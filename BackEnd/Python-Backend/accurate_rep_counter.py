"""
🔧 CRITICAL FIX: Accurate Repetition Counter - Stop False Positives
Enhanced rep counter with strict validation to prevent false positives
"""

import cv2
import numpy as np
import mediapipe as mp
import math
from collections import deque
import time
from typing import Dict, List, Tuple, Optional

class AccurateRepCounter:
    """
    Enhanced repetition counter with strict validation to prevent false positives
    """
    
    def __init__(self, exercise_type="pushups"):
        self.exercise_type = exercise_type
        self.rep_count = 0
        self.current_state = "NONE"  # NONE, UP, DOWN, TRANSITION
        self.last_state_change = time.time()
        
        # Anti-false positive settings
        self.min_angle_change = 15  # Minimum angle change to register movement
        self.min_hold_time = 0.3    # Minimum time to hold position (seconds)
        self.max_movement_speed = 200  # Max degrees per second (prevents jitter)
        self.pose_confidence_threshold = 0.8
        self.visibility_threshold = 0.7
        
        # Movement tracking
        self.angle_history = deque(maxlen=10)
        self.timestamp_history = deque(maxlen=10)
        self.last_valid_angle = None
        self.movement_buffer = deque(maxlen=5)
        
        # Exercise-specific thresholds from reference table
        self.exercise_thresholds = {
            "pushups": {
                "up_angle": 160,    # Elbow extended
                "down_angle": 90,   # Bottom position
                "transition_range": 30
            },
            "squats": {
                "up_angle": 32,     # Standing
                "down_angle": 90,   # Squat depth
                "transition_range": 25
            },
            "situps": {
                "up_angle": 80,     # Sitting up
                "down_angle": 10,   # Lying down
                "transition_range": 20
            },
            "plank": {
                "up_angle": 175,    # Body straight
                "down_angle": 165,  # Slight sag allowed
                "transition_range": 10
            },
            "burpee": {
                "up_angle": 160,    # Standing/jump position
                "down_angle": 45,   # Bottom position
                "transition_range": 35
            }
        }
        
        # Initialize MediaPipe pose
        self.mp_pose = mp.solutions.pose
        
    def calculate_angle(self, point1, point2, point3):
        """Calculate angle between three points with error handling"""
        try:
            # Check if points are valid
            if any(p is None for p in [point1, point2, point3]):
                return None
                
            # Convert to numpy arrays
            a = np.array([point1.x, point1.y])
            b = np.array([point2.x, point2.y])
            c = np.array([point3.x, point3.y])
            
            # Calculate vectors
            ba = a - b
            bc = c - b
            
            # Prevent division by zero
            norm_ba = np.linalg.norm(ba)
            norm_bc = np.linalg.norm(bc)
            
            if norm_ba == 0 or norm_bc == 0:
                return None
            
            # Calculate angle
            cosine_angle = np.dot(ba, bc) / (norm_ba * norm_bc)
            cosine_angle = np.clip(cosine_angle, -1.0, 1.0)
            angle = np.arccos(cosine_angle)
            
            return np.degrees(angle)
        except:
            return None
    
    def validate_pose_detection(self, landmarks):
        """Validate if pose detection is reliable"""
        if not landmarks:
            return False
            
        # Check key landmarks based on exercise
        key_landmarks = self.get_key_landmarks_for_exercise()
        
        for landmark_idx in key_landmarks:
            if landmark_idx < len(landmarks.landmark):
                landmark = landmarks.landmark[landmark_idx]
                if landmark.visibility < self.visibility_threshold:
                    return False
            else:
                return False
                
        return True
    
    def get_key_landmarks_for_exercise(self):
        """Get critical landmarks for each exercise"""
        landmark_map = {
            "pushups": [
                self.mp_pose.PoseLandmark.LEFT_SHOULDER.value,
                self.mp_pose.PoseLandmark.LEFT_ELBOW.value,
                self.mp_pose.PoseLandmark.LEFT_WRIST.value,
                self.mp_pose.PoseLandmark.RIGHT_SHOULDER.value,
                self.mp_pose.PoseLandmark.RIGHT_ELBOW.value,
                self.mp_pose.PoseLandmark.RIGHT_WRIST.value
            ],
            "squats": [
                self.mp_pose.PoseLandmark.LEFT_HIP.value,
                self.mp_pose.PoseLandmark.LEFT_KNEE.value,
                self.mp_pose.PoseLandmark.LEFT_ANKLE.value,
                self.mp_pose.PoseLandmark.RIGHT_HIP.value,
                self.mp_pose.PoseLandmark.RIGHT_KNEE.value,
                self.mp_pose.PoseLandmark.RIGHT_ANKLE.value
            ],
            "situps": [
                self.mp_pose.PoseLandmark.LEFT_SHOULDER.value,
                self.mp_pose.PoseLandmark.LEFT_HIP.value,
                self.mp_pose.PoseLandmark.LEFT_KNEE.value
            ],
            "plank": [
                self.mp_pose.PoseLandmark.LEFT_SHOULDER.value,
                self.mp_pose.PoseLandmark.LEFT_HIP.value,
                self.mp_pose.PoseLandmark.LEFT_ANKLE.value
            ],
            "burpee": [
                self.mp_pose.PoseLandmark.LEFT_SHOULDER.value,
                self.mp_pose.PoseLandmark.LEFT_ELBOW.value,
                self.mp_pose.PoseLandmark.LEFT_HIP.value,
                self.mp_pose.PoseLandmark.LEFT_KNEE.value
            ]
        }
        
        return landmark_map.get(self.exercise_type, [])
    
    def calculate_exercise_angle(self, landmarks):
        """Calculate the primary angle for the exercise"""
        try:
            if self.exercise_type == "pushups":
                # Use elbow angle (shoulder-elbow-wrist)
                shoulder = landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_SHOULDER.value]
                elbow = landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_ELBOW.value]
                wrist = landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_WRIST.value]
                return self.calculate_angle(shoulder, elbow, wrist)
                
            elif self.exercise_type == "squats":
                # Use knee angle (hip-knee-ankle)
                hip = landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_HIP.value]
                knee = landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_KNEE.value]
                ankle = landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_ANKLE.value]
                return self.calculate_angle(hip, knee, ankle)
                
            elif self.exercise_type == "situps":
                # Use torso angle (shoulder-hip-knee)
                shoulder = landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_SHOULDER.value]
                hip = landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_HIP.value]
                knee = landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_KNEE.value]
                return self.calculate_angle(shoulder, hip, knee)
                
            elif self.exercise_type == "plank":
                # Use body line angle (shoulder-hip-ankle)
                shoulder = landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_SHOULDER.value]
                hip = landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_HIP.value]
                ankle = landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_ANKLE.value]
                return self.calculate_angle(shoulder, hip, ankle)
                
            elif self.exercise_type == "burpee":
                # Use knee angle for squat phase
                hip = landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_HIP.value]
                knee = landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_KNEE.value]
                ankle = landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_ANKLE.value]
                return self.calculate_angle(hip, knee, ankle)
                
        except Exception as e:
            return None
            
        return None
    
    def validate_movement_speed(self, current_angle):
        """Prevent counting rapid jitter movements"""
        current_time = time.time()
        
        if len(self.angle_history) > 0 and len(self.timestamp_history) > 0:
            time_diff = current_time - self.timestamp_history[-1]
            angle_diff = abs(current_angle - self.angle_history[-1])
            
            if time_diff > 0:
                movement_speed = angle_diff / time_diff
                if movement_speed > self.max_movement_speed:
                    return False  # Too fast, likely jitter
                    
        return True
    
    def determine_exercise_state(self, angle):
        """Determine current exercise state with strict validation"""
        if angle is None:
            return "NONE"
            
        if self.exercise_type not in self.exercise_thresholds:
            return "NONE"
            
        thresholds = self.exercise_thresholds[self.exercise_type]
        up_threshold = thresholds["up_angle"]
        down_threshold = thresholds["down_angle"]
        transition_range = thresholds["transition_range"]
        
        # For exercises where UP position has higher angle (like pushups, situps)
        if self.exercise_type in ["pushups", "situps", "plank"]:
            if angle >= up_threshold - transition_range:
                return "UP"
            elif angle <= down_threshold + transition_range:
                return "DOWN"
            else:
                return "TRANSITION"
        
        # For exercises where DOWN position has higher angle (like squats, burpees)
        else:  # squats, burpees
            if angle <= up_threshold + transition_range:
                return "UP"
            elif angle >= down_threshold - transition_range:
                return "DOWN"
            else:
                return "TRANSITION"
    
    def update_rep_count(self, landmarks):
        """Main function to update repetition count with all validations"""
        
        # Step 1: Validate pose detection quality
        if not self.validate_pose_detection(landmarks):
            self.current_state = "NONE"
            return {
                'rep_count': self.rep_count,
                'current_state': self.current_state,
                'debug_info': "Poor pose detection",
                'angle': None,
                'confidence': 0.0
            }
        
        # Step 2: Calculate exercise-specific angle
        current_angle = self.calculate_exercise_angle(landmarks)
        if current_angle is None:
            self.current_state = "NONE"
            return {
                'rep_count': self.rep_count,
                'current_state': self.current_state,
                'debug_info': "Cannot calculate angle",
                'angle': None,
                'confidence': 0.0
            }
        
        # Step 3: Validate movement speed (prevent jitter)
        if not self.validate_movement_speed(current_angle):
            return {
                'rep_count': self.rep_count,
                'current_state': self.current_state,
                'debug_info': "Movement too fast (jitter detected)",
                'angle': current_angle,
                'confidence': 0.5
            }
        
        # Step 4: Update angle history
        current_time = time.time()
        self.angle_history.append(current_angle)
        self.timestamp_history.append(current_time)
        
        # Step 5: Determine current state
        new_state = self.determine_exercise_state(current_angle)
        
        # Step 6: Validate state change timing
        if new_state != self.current_state:
            time_since_change = current_time - self.last_state_change
            if time_since_change < self.min_hold_time:
                # State changed too quickly, ignore
                return {
                    'rep_count': self.rep_count,
                    'current_state': self.current_state,
                    'debug_info': f"State change too fast ({time_since_change:.2f}s)",
                    'angle': current_angle,
                    'confidence': 0.3
                }
            
            # Valid state change
            old_state = self.current_state
            self.current_state = new_state
            self.last_state_change = current_time
            
            # Step 7: Count repetition on valid UP→DOWN→UP cycle
            if old_state == "DOWN" and new_state == "UP":
                # Ensure minimum angle change occurred
                if len(self.angle_history) >= 5:
                    recent_angles = list(self.angle_history)[-5:]
                    angle_range = max(recent_angles) - min(recent_angles)
                    if angle_range >= self.min_angle_change:
                        self.rep_count += 1
                        return {
                            'rep_count': self.rep_count,
                            'current_state': self.current_state,
                            'debug_info': f"Valid rep! Angle range: {angle_range:.1f}°",
                            'angle': current_angle,
                            'confidence': 1.0
                        }
        
        return {
            'rep_count': self.rep_count,
            'current_state': self.current_state,
            'debug_info': f"Angle: {current_angle:.1f}°",
            'angle': current_angle,
            'confidence': 0.8
        }
    
    def reset_counter(self):
        """Reset the rep counter"""
        self.rep_count = 0
        self.current_state = "NONE"
        self.angle_history.clear()
        self.timestamp_history.clear()
        self.last_state_change = time.time()
    
    def get_stats(self):
        """Get detailed statistics"""
        return {
            'rep_count': self.rep_count,
            'current_state': self.current_state,
            'exercise_type': self.exercise_type,
            'angle_history_size': len(self.angle_history),
            'last_angle': self.angle_history[-1] if self.angle_history else None,
            'thresholds': self.exercise_thresholds.get(self.exercise_type, {})
        }

# Enhanced demo with accurate rep counting
def accurate_rep_counter_demo():
    """Demo with enhanced accurate rep counting"""
    print("🔧 ACCURATE REP COUNTER DEMO")
    print("=" * 50)
    print("This demo prevents false positives and ensures accurate counting")
    print("Controls: 'q' to quit, 'r' to reset counter")
    print("Try standing still - should show 0 reps!")
    print()
    
    # Initialize MediaPipe with higher confidence thresholds
    mp_pose = mp.solutions.pose
    pose = mp_pose.Pose(
        static_image_mode=False,
        model_complexity=1,
        smooth_landmarks=True,
        enable_segmentation=False,
        smooth_segmentation=True,
        min_detection_confidence=0.8,  # Higher confidence
        min_tracking_confidence=0.8    # Higher tracking confidence
    )
    
    mp_drawing = mp.solutions.drawing_utils
    
    # Initialize accurate rep counter
    exercise = input("Enter exercise type (pushups/squats/situps/plank/burpee) [pushups]: ").strip().lower()
    if not exercise:
        exercise = "pushups"
    
    rep_counter = AccurateRepCounter(exercise)
    
    cap = cv2.VideoCapture(0)
    
    if not cap.isOpened():
        print("❌ Error: Could not open camera")
        return
    
    print(f"✅ Starting accurate rep counter for: {exercise}")
    print("Stand still first to verify 0 false positives!")
    
    try:
        while cap.isOpened():
            ret, frame = cap.read()
            if not ret:
                break
                
            # Convert to RGB
            rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            results = pose.process(rgb_frame)
            
            # Draw pose landmarks
            if results.pose_landmarks:
                mp_drawing.draw_landmarks(
                    frame, results.pose_landmarks, mp_pose.POSE_CONNECTIONS)
                
                # Update rep count with all validations
                rep_data = rep_counter.update_rep_count(results.pose_landmarks)
                
                # Display results with enhanced info
                cv2.putText(frame, f"Reps: {rep_data['rep_count']}", (10, 30), 
                           cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 3)
                cv2.putText(frame, f"State: {rep_data['current_state']}", (10, 70), 
                           cv2.FONT_HERSHEY_SIMPLEX, 0.7, (255, 0, 0), 2)
                cv2.putText(frame, f"Exercise: {exercise.upper()}", (10, 110), 
                           cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255, 255, 0), 2)
                
                # Display angle if available
                if rep_data['angle']:
                    cv2.putText(frame, f"Angle: {rep_data['angle']:.1f}°", (10, 150), 
                               cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 255, 255), 2)
                
                # Display confidence
                confidence_color = (0, 255, 0) if rep_data['confidence'] > 0.7 else (0, 0, 255)
                cv2.putText(frame, f"Confidence: {rep_data['confidence']:.1f}", (10, 190), 
                           cv2.FONT_HERSHEY_SIMPLEX, 0.5, confidence_color, 2)
                
                # Display debug info
                cv2.putText(frame, rep_data['debug_info'], (10, 230), 
                           cv2.FONT_HERSHEY_SIMPLEX, 0.4, (255, 255, 255), 1)
                
            else:
                cv2.putText(frame, "No pose detected", (10, 30), 
                           cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2)
                cv2.putText(frame, "Stand in front of camera", (10, 70), 
                           cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)
            
            # Display instructions
            cv2.putText(frame, "Press 'q' to quit, 'r' to reset", (10, frame.shape[0] - 20), 
                       cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)
            
            cv2.imshow('🔧 Accurate Rep Counter - SIH Demo', frame)
            
            key = cv2.waitKey(1) & 0xFF
            if key == ord('q'):
                break
            elif key == ord('r'):
                rep_counter.reset_counter()
                print("🔄 Counter reset!")
                
    except KeyboardInterrupt:
        print("\n⏹️ Demo stopped by user")
    finally:
        cap.release()
        cv2.destroyAllWindows()
        pose.close()
        
        # Display final stats
        stats = rep_counter.get_stats()
        print(f"\n📊 FINAL STATS:")
        print(f"Exercise: {stats['exercise_type']}")
        print(f"Total Reps: {stats['rep_count']}")
        print(f"Final State: {stats['current_state']}")
        print(f"Thresholds Used: {stats['thresholds']}")
        
        print("\n✅ Accurate Rep Counter Demo Completed!")

if __name__ == "__main__":
    accurate_rep_counter_demo()
