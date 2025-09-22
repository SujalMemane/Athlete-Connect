"""
Core Pose Detection System
Real-time pose detection using MediaPipe BlazePose
"""

import cv2
import mediapipe as mp
import numpy as np
from typing import List, Tuple, Optional, Dict
import math
import time

class PoseDetector:
    """
    Advanced pose detection system with real-time analysis capabilities
    """
    
    def __init__(self, 
                 static_image_mode: bool = False,
                 model_complexity: int = 1,
                 smooth_landmarks: bool = True,
                 enable_segmentation: bool = False,
                 smooth_segmentation: bool = True,
                 min_detection_confidence: float = 0.5,
                 min_tracking_confidence: float = 0.5):
        
        self.mp_pose = mp.solutions.pose
        self.mp_drawing = mp.solutions.drawing_utils
        self.mp_drawing_styles = mp.solutions.drawing_styles
        
        self.pose = self.mp_pose.Pose(
            static_image_mode=static_image_mode,
            model_complexity=model_complexity,
            smooth_landmarks=smooth_landmarks,
            enable_segmentation=enable_segmentation,
            smooth_segmentation=smooth_segmentation,
            min_detection_confidence=min_detection_confidence,
            min_tracking_confidence=min_tracking_confidence
        )
        
        # Performance tracking
        self.fps_counter = 0
        self.fps_start_time = time.time()
        self.current_fps = 0
        
        # Landmark smoothing buffer
        self.landmark_buffer = []
        self.buffer_size = 5
        
    def detect_pose(self, image: np.ndarray, draw: bool = True) -> Tuple[np.ndarray, Optional[List]]:
        """
        Detect pose landmarks in image
        
        Args:
            image: Input image (BGR format)
            draw: Whether to draw pose landmarks
            
        Returns:
            Tuple of (processed_image, landmarks_list)
        """
        # Convert BGR to RGB
        image_rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        image_rgb.flags.writeable = False
        
        # Process pose detection
        results = self.pose.process(image_rgb)
        
        # Convert back to BGR
        image_rgb.flags.writeable = True
        image_bgr = cv2.cvtColor(image_rgb, cv2.COLOR_RGB2BGR)
        
        landmarks = None
        if results.pose_landmarks:
            landmarks = self._extract_landmarks(results.pose_landmarks)
            
            # Apply smoothing
            landmarks = self._smooth_landmarks(landmarks)
            
            if draw:
                self._draw_landmarks(image_bgr, results.pose_landmarks)
                
        # Update FPS
        self._update_fps()
        
        return image_bgr, landmarks
    
    def get_raw_landmarks(self, image: np.ndarray):
        """Get raw MediaPipe pose landmarks for rep counter"""
        # Convert BGR to RGB
        image_rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        image_rgb.flags.writeable = False
        
        # Process pose detection
        results = self.pose.process(image_rgb)
        
        # Return raw landmarks if detected
        if results.pose_landmarks:
            return results.pose_landmarks
        return None
    
    def _extract_landmarks(self, pose_landmarks) -> List[Dict]:
        """Extract landmark coordinates and visibility"""
        landmarks = []
        for idx, landmark in enumerate(pose_landmarks.landmark):
            landmarks.append({
                'id': idx,
                'x': landmark.x,
                'y': landmark.y,
                'z': landmark.z,
                'visibility': landmark.visibility
            })
        return landmarks
    
    def _smooth_landmarks(self, landmarks: List[Dict]) -> List[Dict]:
        """Apply temporal smoothing to landmarks"""
        if not landmarks:
            return landmarks
            
        self.landmark_buffer.append(landmarks)
        if len(self.landmark_buffer) > self.buffer_size:
            self.landmark_buffer.pop(0)
            
        if len(self.landmark_buffer) < 2:
            return landmarks
            
        # Average landmarks over buffer
        smoothed_landmarks = []
        for i in range(len(landmarks)):
            x_sum = sum(frame[i]['x'] for frame in self.landmark_buffer)
            y_sum = sum(frame[i]['y'] for frame in self.landmark_buffer)
            z_sum = sum(frame[i]['z'] for frame in self.landmark_buffer)
            vis_sum = sum(frame[i]['visibility'] for frame in self.landmark_buffer)
            
            buffer_len = len(self.landmark_buffer)
            smoothed_landmarks.append({
                'id': i,
                'x': x_sum / buffer_len,
                'y': y_sum / buffer_len,
                'z': z_sum / buffer_len,
                'visibility': vis_sum / buffer_len
            })
            
        return smoothed_landmarks
    
    def _draw_landmarks(self, image: np.ndarray, pose_landmarks):
        """Draw pose landmarks with custom styling"""
        self.mp_drawing.draw_landmarks(
            image,
            pose_landmarks,
            self.mp_pose.POSE_CONNECTIONS,
            landmark_drawing_spec=self.mp_drawing_styles.get_default_pose_landmarks_style()
        )
    
    def calculate_angle(self, landmarks: List[Dict], point1_id: int, point2_id: int, point3_id: int) -> float:
        """
        Calculate angle between three points
        
        Args:
            landmarks: List of landmark dictionaries
            point1_id, point2_id, point3_id: Landmark IDs
            
        Returns:
            Angle in degrees
        """
        if not landmarks or len(landmarks) <= max(point1_id, point2_id, point3_id):
            return 0.0
            
        # Get coordinates
        p1 = np.array([landmarks[point1_id]['x'], landmarks[point1_id]['y']])
        p2 = np.array([landmarks[point2_id]['x'], landmarks[point2_id]['y']])
        p3 = np.array([landmarks[point3_id]['x'], landmarks[point3_id]['y']])
        
        # Calculate vectors
        v1 = p1 - p2
        v2 = p3 - p2
        
        # Calculate angle
        cos_angle = np.dot(v1, v2) / (np.linalg.norm(v1) * np.linalg.norm(v2))
        cos_angle = np.clip(cos_angle, -1.0, 1.0)  # Prevent numerical errors
        angle = np.arccos(cos_angle)
        
        return np.degrees(angle)
    
    def get_distance(self, landmarks: List[Dict], point1_id: int, point2_id: int) -> float:
        """Calculate Euclidean distance between two landmarks"""
        if not landmarks or len(landmarks) <= max(point1_id, point2_id):
            return 0.0
            
        p1 = np.array([landmarks[point1_id]['x'], landmarks[point1_id]['y'], landmarks[point1_id]['z']])
        p2 = np.array([landmarks[point2_id]['x'], landmarks[point2_id]['y'], landmarks[point2_id]['z']])
        
        return np.linalg.norm(p1 - p2)
    
    def is_landmark_visible(self, landmarks: List[Dict], landmark_id: int, threshold: float = 0.5) -> bool:
        """Check if landmark is visible above threshold"""
        if not landmarks or len(landmarks) <= landmark_id:
            return False
        return landmarks[landmark_id]['visibility'] > threshold
    
    def _update_fps(self):
        """Update FPS counter"""
        self.fps_counter += 1
        current_time = time.time()
        
        if current_time - self.fps_start_time >= 1.0:
            self.current_fps = self.fps_counter
            self.fps_counter = 0
            self.fps_start_time = current_time
    
    def get_fps(self) -> int:
        """Get current FPS"""
        return self.current_fps
    
    def get_body_part_positions(self, landmarks: List[Dict]) -> Dict[str, Dict]:
        """
        Get organized body part positions
        
        Returns:
            Dictionary with body part positions
        """
        if not landmarks:
            return {}
            
        body_parts = {
            'head': {
                'nose': landmarks[0] if len(landmarks) > 0 else None,
                'left_eye': landmarks[1] if len(landmarks) > 1 else None,
                'right_eye': landmarks[2] if len(landmarks) > 2 else None,
                'left_ear': landmarks[7] if len(landmarks) > 7 else None,
                'right_ear': landmarks[8] if len(landmarks) > 8 else None,
            },
            'torso': {
                'left_shoulder': landmarks[11] if len(landmarks) > 11 else None,
                'right_shoulder': landmarks[12] if len(landmarks) > 12 else None,
                'left_hip': landmarks[23] if len(landmarks) > 23 else None,
                'right_hip': landmarks[24] if len(landmarks) > 24 else None,
            },
            'arms': {
                'left_elbow': landmarks[13] if len(landmarks) > 13 else None,
                'right_elbow': landmarks[14] if len(landmarks) > 14 else None,
                'left_wrist': landmarks[15] if len(landmarks) > 15 else None,
                'right_wrist': landmarks[16] if len(landmarks) > 16 else None,
            },
            'legs': {
                'left_knee': landmarks[25] if len(landmarks) > 25 else None,
                'right_knee': landmarks[26] if len(landmarks) > 26 else None,
                'left_ankle': landmarks[27] if len(landmarks) > 27 else None,
                'right_ankle': landmarks[28] if len(landmarks) > 28 else None,
            }
        }
        
        return body_parts
    
    def cleanup(self):
        """Cleanup resources"""
        if hasattr(self, 'pose'):
            self.pose.close()

# Example usage and testing
if __name__ == "__main__":
    # Initialize pose detector
    detector = PoseDetector()
    
    # Test with webcam
    cap = cv2.VideoCapture(0)
    
    print("Starting pose detection demo...")
    print("Press 'q' to quit")
    
    try:
        while cap.isOpened():
            ret, frame = cap.read()
            if not ret:
                break
                
            # Detect pose
            processed_frame, landmarks = detector.detect_pose(frame)
            
            # Display FPS
            fps = detector.get_fps()
            cv2.putText(processed_frame, f'FPS: {fps}', (10, 30), 
                       cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
            
            # Display frame
            cv2.imshow('SIH Sports Pose Detection', processed_frame)
            
            if cv2.waitKey(1) & 0xFF == ord('q'):
                break
                
    except KeyboardInterrupt:
        print("\nDemo stopped by user")
    finally:
        cap.release()
        cv2.destroyAllWindows()
        detector.cleanup()
        print("Pose detection demo completed")

