"""
Advanced Repetition Counter with Exercise-Specific Logic
High-accuracy rep counting with adaptive thresholds and pattern recognition
"""

import numpy as np
from typing import Dict, List, Tuple, Optional
import math
from enum import Enum
from collections import deque
import time

class ExerciseType(Enum):
    PUSHUP = "pushup"
    SQUAT = "squat"
    SITUP = "situp"
    PLANK = "plank"
    BURPEE = "burpee"
    VERTICAL_JUMP = "vertical_jump"

class RepState(Enum):
    NEUTRAL = "neutral"
    DOWN = "down"
    UP = "up"
    TRANSITION = "transition"

class ImprovedRepCounter:
    """
    Advanced repetition counter with exercise-specific logic and adaptive thresholds
    """
    
    def __init__(self, exercise_type: ExerciseType, sensitivity: float = 0.7):
        self.exercise_type = exercise_type
        self.sensitivity = sensitivity
        
        # Rep counting state
        self.rep_count = 0
        self.current_state = RepState.NEUTRAL
        self.last_state_change = time.time()
        
        # Movement tracking
        self.position_history = deque(maxlen=30)  # 1 second at 30fps
        self.angle_history = deque(maxlen=30)
        self.velocity_history = deque(maxlen=15)
        
        # Adaptive thresholds
        self.thresholds = self._get_exercise_thresholds()
        self.adaptive_threshold_up = self.thresholds['up']
        self.adaptive_threshold_down = self.thresholds['down']
        
        # Quality metrics
        self.rep_quality_scores = []
        self.rep_durations = []
        self.last_rep_time = None
        
        # Anti-cheat measures
        self.min_rep_duration = 0.5  # Minimum time for valid rep
        self.max_rep_duration = 10.0  # Maximum time for valid rep
        self.noise_threshold = 0.05
        
    def _get_exercise_thresholds(self) -> Dict[str, float]:
        """Get exercise-specific thresholds"""
        thresholds_map = {
            ExerciseType.PUSHUP: {'up': 160, 'down': 90, 'key_angle': 'elbow'},
            ExerciseType.SQUAT: {'up': 160, 'down': 90, 'key_angle': 'knee'},
            ExerciseType.SITUP: {'up': 45, 'down': 15, 'key_angle': 'hip'},
            ExerciseType.PLANK: {'up': 175, 'down': 165, 'key_angle': 'hip'},
            ExerciseType.BURPEE: {'up': 160, 'down': 45, 'key_angle': 'knee'},
            ExerciseType.VERTICAL_JUMP: {'up': 0.3, 'down': -0.1, 'key_angle': 'hip_height'}
        }
        return thresholds_map.get(self.exercise_type, {'up': 160, 'down': 90, 'key_angle': 'elbow'})
    
    def update(self, landmarks: List[Dict], timestamp: Optional[float] = None) -> Dict:
        """
        Update rep counter with new pose landmarks
        
        Args:
            landmarks: Pose landmarks from pose detector
            timestamp: Optional timestamp (uses current time if None)
            
        Returns:
            Dictionary with rep count and analysis data
        """
        if timestamp is None:
            timestamp = time.time()
            
        if not landmarks:
            return self._get_status()
            
        # Calculate key metrics based on exercise type
        key_metric = self._calculate_key_metric(landmarks)
        if key_metric is None:
            return self._get_status()
            
        # Update history
        self._update_history(key_metric, timestamp)
        
        # Calculate movement velocity
        velocity = self._calculate_velocity()
        
        # Detect rep state changes
        new_state = self._detect_state_change(key_metric, velocity)
        
        # Update rep count if valid transition
        if self._is_valid_transition(new_state, timestamp):
            self._update_rep_count(new_state, timestamp)
            
        # Adapt thresholds based on performance
        self._adapt_thresholds()
        
        return self._get_status()
    
    def _calculate_key_metric(self, landmarks: List[Dict]) -> Optional[float]:
        """Calculate the key metric for the specific exercise"""
        try:
            if self.exercise_type == ExerciseType.PUSHUP:
                return self._calculate_elbow_angle(landmarks)
            elif self.exercise_type == ExerciseType.SQUAT:
                return self._calculate_knee_angle(landmarks)
            elif self.exercise_type == ExerciseType.SITUP:
                return self._calculate_hip_angle(landmarks)
            elif self.exercise_type == ExerciseType.PLANK:
                return self._calculate_plank_angle(landmarks)
            elif self.exercise_type == ExerciseType.BURPEE:
                return self._calculate_burpee_metric(landmarks)
            elif self.exercise_type == ExerciseType.VERTICAL_JUMP:
                return self._calculate_jump_height(landmarks)
        except (IndexError, KeyError, TypeError):
            return None
        
        return None
    
    def _calculate_elbow_angle(self, landmarks: List[Dict]) -> float:
        """Calculate average elbow angle for push-ups"""
        if len(landmarks) < 17:
            return 0.0
            
        # Left elbow angle (shoulder-elbow-wrist)
        left_angle = self._angle_between_points(
            landmarks[11], landmarks[13], landmarks[15]  # left shoulder, elbow, wrist
        )
        
        # Right elbow angle
        right_angle = self._angle_between_points(
            landmarks[12], landmarks[14], landmarks[16]  # right shoulder, elbow, wrist
        )
        
        return (left_angle + right_angle) / 2
    
    def _calculate_knee_angle(self, landmarks: List[Dict]) -> float:
        """Calculate average knee angle for squats"""
        if len(landmarks) < 28:
            return 0.0
            
        # Left knee angle (hip-knee-ankle)
        left_angle = self._angle_between_points(
            landmarks[23], landmarks[25], landmarks[27]  # left hip, knee, ankle
        )
        
        # Right knee angle
        right_angle = self._angle_between_points(
            landmarks[24], landmarks[26], landmarks[28]  # right hip, knee, ankle
        )
        
        return (left_angle + right_angle) / 2
    
    def _calculate_hip_angle(self, landmarks: List[Dict]) -> float:
        """Calculate hip angle for sit-ups"""
        if len(landmarks) < 26:
            return 0.0
            
        # Hip angle using shoulder-hip-knee
        left_angle = self._angle_between_points(
            landmarks[11], landmarks[23], landmarks[25]  # left shoulder, hip, knee
        )
        
        right_angle = self._angle_between_points(
            landmarks[12], landmarks[24], landmarks[26]  # right shoulder, hip, knee
        )
        
        return (left_angle + right_angle) / 2
    
    def _calculate_plank_angle(self, landmarks: List[Dict]) -> float:
        """Calculate body alignment angle for plank"""
        if len(landmarks) < 28:
            return 0.0
            
        # Body line angle (shoulder-hip-ankle)
        return self._angle_between_points(
            landmarks[11], landmarks[23], landmarks[27]  # left shoulder, hip, ankle
        )
    
    def _calculate_burpee_metric(self, landmarks: List[Dict]) -> float:
        """Calculate combined metric for burpees"""
        if len(landmarks) < 28:
            return 0.0
            
        # Use knee angle as primary metric
        return self._calculate_knee_angle(landmarks)
    
    def _calculate_jump_height(self, landmarks: List[Dict]) -> float:
        """Calculate relative jump height"""
        if len(landmarks) < 24:
            return 0.0
            
        # Use hip height as metric
        left_hip_y = landmarks[23]['y']
        right_hip_y = landmarks[24]['y']
        return -(left_hip_y + right_hip_y) / 2  # Negative because y increases downward
    
    def _angle_between_points(self, p1: Dict, p2: Dict, p3: Dict) -> float:
        """Calculate angle between three points"""
        # Convert to numpy arrays
        a = np.array([p1['x'], p1['y']])
        b = np.array([p2['x'], p2['y']])
        c = np.array([p3['x'], p3['y']])
        
        # Calculate vectors
        ba = a - b
        bc = c - b
        
        # Calculate angle
        cosine_angle = np.dot(ba, bc) / (np.linalg.norm(ba) * np.linalg.norm(bc))
        cosine_angle = np.clip(cosine_angle, -1.0, 1.0)
        angle = np.arccos(cosine_angle)
        
        return np.degrees(angle)
    
    def _update_history(self, key_metric: float, timestamp: float):
        """Update movement history"""
        self.position_history.append({
            'value': key_metric,
            'timestamp': timestamp
        })
        
        if len(self.position_history) > 1:
            self.angle_history.append(key_metric)
    
    def _calculate_velocity(self) -> float:
        """Calculate movement velocity"""
        if len(self.position_history) < 2:
            return 0.0
            
        recent = list(self.position_history)[-5:]  # Last 5 frames
        if len(recent) < 2:
            return 0.0
            
        # Calculate average velocity over recent frames
        velocities = []
        for i in range(1, len(recent)):
            dt = recent[i]['timestamp'] - recent[i-1]['timestamp']
            if dt > 0:
                dv = recent[i]['value'] - recent[i-1]['value']
                velocities.append(dv / dt)
        
        return np.mean(velocities) if velocities else 0.0
    
    def _detect_state_change(self, key_metric: float, velocity: float) -> RepState:
        """Detect current movement state"""
        # Use adaptive thresholds
        if key_metric < self.adaptive_threshold_down:
            return RepState.DOWN
        elif key_metric > self.adaptive_threshold_up:
            return RepState.UP
        else:
            # Check velocity for transition detection
            if abs(velocity) > self.noise_threshold:
                return RepState.TRANSITION
            else:
                return self.current_state
    
    def _is_valid_transition(self, new_state: RepState, timestamp: float) -> bool:
        """Check if state transition is valid"""
        if new_state == self.current_state:
            return False
            
        # Check minimum duration since last state change
        time_since_change = timestamp - self.last_state_change
        if time_since_change < 0.2:  # Minimum 200ms between state changes
            return False
            
        # Valid transitions for rep counting
        valid_transitions = {
            (RepState.UP, RepState.DOWN),
            (RepState.DOWN, RepState.UP),
            (RepState.NEUTRAL, RepState.DOWN),
            (RepState.NEUTRAL, RepState.UP)
        }
        
        return (self.current_state, new_state) in valid_transitions
    
    def _update_rep_count(self, new_state: RepState, timestamp: float):
        """Update rep count for valid transitions"""
        # Count reps on up-to-down or down-to-up transitions
        if ((self.current_state == RepState.UP and new_state == RepState.DOWN) or
            (self.current_state == RepState.DOWN and new_state == RepState.UP)):
            
            # Check rep duration validity
            if self.last_rep_time:
                rep_duration = timestamp - self.last_rep_time
                if self.min_rep_duration <= rep_duration <= self.max_rep_duration:
                    self.rep_count += 1
                    self.rep_durations.append(rep_duration)
                    self._calculate_rep_quality()
            else:
                # First rep
                self.rep_count += 1
                
            self.last_rep_time = timestamp
        
        # Update state
        self.current_state = new_state
        self.last_state_change = timestamp
    
    def _calculate_rep_quality(self):
        """Calculate quality score for the last rep"""
        if len(self.angle_history) < 10:
            return
            
        # Analyze movement smoothness and range
        recent_angles = list(self.angle_history)[-15:]
        
        # Range of motion score
        angle_range = max(recent_angles) - min(recent_angles)
        expected_range = abs(self.thresholds['up'] - self.thresholds['down'])
        range_score = min(angle_range / expected_range, 1.0)
        
        # Smoothness score (lower variance is better)
        smoothness = 1.0 / (1.0 + np.var(recent_angles))
        
        # Combined quality score
        quality_score = (range_score * 0.7 + smoothness * 0.3) * 100
        self.rep_quality_scores.append(quality_score)
    
    def _adapt_thresholds(self):
        """Adapt thresholds based on user's movement patterns"""
        if len(self.angle_history) < 20:
            return
            
        recent_angles = list(self.angle_history)[-20:]
        
        # Calculate percentiles for adaptive thresholds
        p25 = np.percentile(recent_angles, 25)
        p75 = np.percentile(recent_angles, 75)
        
        # Smooth adaptation
        adaptation_rate = 0.1
        self.adaptive_threshold_down = (self.adaptive_threshold_down * (1 - adaptation_rate) + 
                                      p25 * adaptation_rate)
        self.adaptive_threshold_up = (self.adaptive_threshold_up * (1 - adaptation_rate) + 
                                    p75 * adaptation_rate)
    
    def _get_status(self) -> Dict:
        """Get current status and metrics"""
        return {
            'rep_count': self.rep_count,
            'current_state': self.current_state.value,
            'average_quality': np.mean(self.rep_quality_scores) if self.rep_quality_scores else 0,
            'average_duration': np.mean(self.rep_durations) if self.rep_durations else 0,
            'reps_per_minute': self._calculate_rpm(),
            'thresholds': {
                'up': self.adaptive_threshold_up,
                'down': self.adaptive_threshold_down
            }
        }
    
    def _calculate_rpm(self) -> float:
        """Calculate reps per minute"""
        if len(self.rep_durations) < 2:
            return 0.0
            
        avg_duration = np.mean(self.rep_durations)
        return 60.0 / avg_duration if avg_duration > 0 else 0.0
    
    def reset(self):
        """Reset counter for new session"""
        self.rep_count = 0
        self.current_state = RepState.NEUTRAL
        self.position_history.clear()
        self.angle_history.clear()
        self.velocity_history.clear()
        self.rep_quality_scores.clear()
        self.rep_durations.clear()
        self.last_rep_time = None
        
        # Reset adaptive thresholds
        self.adaptive_threshold_up = self.thresholds['up']
        self.adaptive_threshold_down = self.thresholds['down']
    
    def get_detailed_stats(self) -> Dict:
        """Get detailed performance statistics"""
        return {
            'total_reps': self.rep_count,
            'average_quality': np.mean(self.rep_quality_scores) if self.rep_quality_scores else 0,
            'quality_consistency': 1.0 / (1.0 + np.std(self.rep_quality_scores)) if len(self.rep_quality_scores) > 1 else 1.0,
            'average_rep_time': np.mean(self.rep_durations) if self.rep_durations else 0,
            'fastest_rep': min(self.rep_durations) if self.rep_durations else 0,
            'slowest_rep': max(self.rep_durations) if self.rep_durations else 0,
            'reps_per_minute': self._calculate_rpm(),
            'exercise_type': self.exercise_type.value,
            'adaptive_thresholds': {
                'up': self.adaptive_threshold_up,
                'down': self.adaptive_threshold_down
            }
        }

# Example usage and testing
if __name__ == "__main__":
    # Test rep counter with different exercises
    exercises = [ExerciseType.PUSHUP, ExerciseType.SQUAT, ExerciseType.SITUP]
    
    for exercise in exercises:
        print(f"\nTesting {exercise.value} rep counter:")
        counter = ImprovedRepCounter(exercise)
        
        # Simulate some rep data
        test_angles = [170, 165, 160, 140, 120, 100, 85, 90, 110, 130, 150, 165, 170]
        
        for i, angle in enumerate(test_angles):
            # Create mock landmarks
            mock_landmarks = [{'x': 0, 'y': 0, 'z': 0} for _ in range(33)]
            
            # Set relevant joint angles based on exercise
            if exercise == ExerciseType.PUSHUP:
                # Simulate elbow angles
                mock_landmarks[13] = {'x': 0.5, 'y': 0.5, 'z': 0}  # Left elbow
                mock_landmarks[14] = {'x': 0.5, 'y': 0.5, 'z': 0}  # Right elbow
            elif exercise == ExerciseType.SQUAT:
                # Simulate knee angles
                mock_landmarks[25] = {'x': 0.5, 'y': 0.5, 'z': 0}  # Left knee
                mock_landmarks[26] = {'x': 0.5, 'y': 0.5, 'z': 0}  # Right knee
            
            result = counter.update(mock_landmarks, time.time() + i * 0.1)
            print(f"Frame {i}: Angle={angle}, Reps={result['rep_count']}, State={result['current_state']}")
        
        # Get final stats
        stats = counter.get_detailed_stats()
        print(f"Final stats: {stats}")
        print("-" * 50)

