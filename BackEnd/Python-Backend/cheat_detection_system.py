"""
Comprehensive Cheat Detection System
Advanced form analysis and violation detection for sports assessments
"""

import numpy as np
from typing import Dict, List, Tuple, Optional, Set
from enum import Enum
import math
import time
from collections import deque, defaultdict

class ViolationType(Enum):
    PARTIAL_ROM = "partial_range_of_motion"
    POOR_FORM = "poor_form"
    BOUNCING = "bouncing"
    ASSISTANCE = "external_assistance"
    INCONSISTENT_TIMING = "inconsistent_timing"
    BODY_ALIGNMENT = "body_alignment"
    SPEED_VIOLATION = "speed_violation"
    INCOMPLETE_REP = "incomplete_rep"

class ExerciseType(Enum):
    PUSHUP = "pushup"
    SQUAT = "squat"
    SITUP = "situp"
    PLANK = "plank"
    BURPEE = "burpee"
    VERTICAL_JUMP = "vertical_jump"

class CheatDetectionSystem:
    """
    Advanced cheat detection system with exercise-specific violation patterns
    """
    
    def __init__(self, exercise_type: ExerciseType, strictness: float = 0.7):
        self.exercise_type = exercise_type
        self.strictness = strictness  # 0.0 (lenient) to 1.0 (strict)
        
        # Violation tracking
        self.violations = defaultdict(int)
        self.violation_history = deque(maxlen=100)
        self.current_violations = set()
        
        # Movement analysis
        self.position_buffer = deque(maxlen=30)
        self.angle_buffer = deque(maxlen=30)
        self.velocity_buffer = deque(maxlen=15)
        self.acceleration_buffer = deque(maxlen=10)
        
        # Exercise-specific parameters
        self.exercise_params = self._get_exercise_parameters()
        
        # Form analysis state
        self.last_analysis_time = time.time()
        self.rep_start_time = None
        self.current_rep_violations = []
        
        # Statistical tracking
        self.form_quality_history = deque(maxlen=50)
        self.confidence_scores = deque(maxlen=20)
        
    def _get_exercise_parameters(self) -> Dict:
        """Get exercise-specific parameters for cheat detection"""
        params = {
            ExerciseType.PUSHUP: {
                'min_range_of_motion': 70,  # degrees
                'max_range_of_motion': 180,
                'min_rep_time': 0.8,  # seconds
                'max_rep_time': 4.0,
                'key_joints': ['elbow', 'shoulder', 'hip'],
                'alignment_joints': ['shoulder', 'hip', 'ankle'],
                'critical_angles': {
                    'elbow_min': 70,
                    'elbow_max': 180,
                    'body_line_min': 160,
                    'body_line_max': 190
                }
            },
            ExerciseType.SQUAT: {
                'min_range_of_motion': 90,
                'max_range_of_motion': 180,
                'min_rep_time': 1.0,
                'max_rep_time': 5.0,
                'key_joints': ['knee', 'hip', 'ankle'],
                'alignment_joints': ['shoulder', 'hip', 'knee'],
                'critical_angles': {
                    'knee_min': 70,
                    'knee_max': 180,
                    'hip_min': 60,
                    'hip_max': 180
                }
            },
            ExerciseType.SITUP: {
                'min_range_of_motion': 30,
                'max_range_of_motion': 90,
                'min_rep_time': 0.6,
                'max_rep_time': 3.0,
                'key_joints': ['hip', 'spine'],
                'alignment_joints': ['shoulder', 'hip', 'knee'],
                'critical_angles': {
                    'hip_min': 30,
                    'hip_max': 90,
                    'spine_min': 160,
                    'spine_max': 190
                }
            },
            ExerciseType.PLANK: {
                'min_range_of_motion': 170,
                'max_range_of_motion': 190,
                'min_rep_time': 10.0,
                'max_rep_time': 300.0,
                'key_joints': ['hip', 'shoulder'],
                'alignment_joints': ['shoulder', 'hip', 'ankle'],
                'critical_angles': {
                    'body_line_min': 165,
                    'body_line_max': 185,
                    'hip_sag_max': 15
                }
            },
            ExerciseType.BURPEE: {
                'min_range_of_motion': 90,
                'max_range_of_motion': 180,
                'min_rep_time': 2.0,
                'max_rep_time': 8.0,
                'key_joints': ['knee', 'elbow', 'hip'],
                'alignment_joints': ['shoulder', 'hip', 'ankle'],
                'critical_angles': {
                    'knee_min': 70,
                    'knee_max': 180,
                    'jump_height_min': 0.1
                }
            },
            ExerciseType.VERTICAL_JUMP: {
                'min_range_of_motion': 0.2,  # meters
                'max_range_of_motion': 2.0,
                'min_rep_time': 0.5,
                'max_rep_time': 3.0,
                'key_joints': ['knee', 'hip', 'ankle'],
                'alignment_joints': ['shoulder', 'hip', 'ankle'],
                'critical_angles': {
                    'knee_prep_min': 70,
                    'landing_stability': 0.5
                }
            }
        }
        
        return params.get(self.exercise_type, params[ExerciseType.PUSHUP])
    
    def analyze_form(self, landmarks: List[Dict], timestamp: Optional[float] = None) -> Dict:
        """
        Analyze form and detect violations
        
        Args:
            landmarks: Pose landmarks from pose detector
            timestamp: Optional timestamp
            
        Returns:
            Dictionary with violation analysis
        """
        if timestamp is None:
            timestamp = time.time()
            
        if not landmarks or len(landmarks) < 25:
            return self._get_analysis_result(confidence=0.0)
            
        # Update movement buffers
        self._update_movement_buffers(landmarks, timestamp)
        
        # Clear current violations for this frame
        self.current_violations.clear()
        
        # Perform exercise-specific analysis
        self._analyze_exercise_specific_form(landmarks)
        
        # Analyze movement patterns
        self._analyze_movement_patterns()
        
        # Analyze timing and consistency
        self._analyze_timing_patterns(timestamp)
        
        # Calculate overall form quality
        form_quality = self._calculate_form_quality()
        confidence = self._calculate_confidence()
        
        # Update history
        self.form_quality_history.append(form_quality)
        self.confidence_scores.append(confidence)
        
        # Record violations
        if self.current_violations:
            self.violation_history.append({
                'timestamp': timestamp,
                'violations': list(self.current_violations),
                'form_quality': form_quality
            })
            
            for violation in self.current_violations:
                self.violations[violation.value] += 1
        
        self.last_analysis_time = timestamp
        
        return self._get_analysis_result(form_quality, confidence)
    
    def _update_movement_buffers(self, landmarks: List[Dict], timestamp: float):
        """Update movement analysis buffers"""
        # Calculate key positions and angles
        key_positions = self._extract_key_positions(landmarks)
        key_angles = self._calculate_key_angles(landmarks)
        
        self.position_buffer.append({
            'timestamp': timestamp,
            'positions': key_positions
        })
        
        self.angle_buffer.append({
            'timestamp': timestamp,
            'angles': key_angles
        })
        
        # Calculate velocities and accelerations
        if len(self.position_buffer) >= 2:
            velocity = self._calculate_velocity()
            self.velocity_buffer.append({
                'timestamp': timestamp,
                'velocity': velocity
            })
            
        if len(self.velocity_buffer) >= 2:
            acceleration = self._calculate_acceleration()
            self.acceleration_buffer.append({
                'timestamp': timestamp,
                'acceleration': acceleration
            })
    
    def _extract_key_positions(self, landmarks: List[Dict]) -> Dict:
        """Extract key body positions for analysis"""
        positions = {}
        
        # Common positions for all exercises
        if len(landmarks) > 24:
            positions.update({
                'left_shoulder': landmarks[11],
                'right_shoulder': landmarks[12],
                'left_hip': landmarks[23],
                'right_hip': landmarks[24],
                'left_knee': landmarks[25] if len(landmarks) > 25 else None,
                'right_knee': landmarks[26] if len(landmarks) > 26 else None,
                'left_ankle': landmarks[27] if len(landmarks) > 27 else None,
                'right_ankle': landmarks[28] if len(landmarks) > 28 else None,
            })
            
        # Exercise-specific positions
        if self.exercise_type in [ExerciseType.PUSHUP, ExerciseType.BURPEE]:
            if len(landmarks) > 16:
                positions.update({
                    'left_elbow': landmarks[13],
                    'right_elbow': landmarks[14],
                    'left_wrist': landmarks[15],
                    'right_wrist': landmarks[16]
                })
        
        return positions
    
    def _calculate_key_angles(self, landmarks: List[Dict]) -> Dict:
        """Calculate key angles for form analysis"""
        angles = {}
        
        try:
            if self.exercise_type == ExerciseType.PUSHUP:
                angles.update({
                    'left_elbow': self._angle_between_points(landmarks[11], landmarks[13], landmarks[15]),
                    'right_elbow': self._angle_between_points(landmarks[12], landmarks[14], landmarks[16]),
                    'body_line': self._angle_between_points(landmarks[11], landmarks[23], landmarks[27])
                })
                
            elif self.exercise_type == ExerciseType.SQUAT:
                angles.update({
                    'left_knee': self._angle_between_points(landmarks[23], landmarks[25], landmarks[27]),
                    'right_knee': self._angle_between_points(landmarks[24], landmarks[26], landmarks[28]),
                    'left_hip': self._angle_between_points(landmarks[11], landmarks[23], landmarks[25]),
                    'right_hip': self._angle_between_points(landmarks[12], landmarks[24], landmarks[26])
                })
                
            elif self.exercise_type == ExerciseType.SITUP:
                angles.update({
                    'hip_angle': self._angle_between_points(landmarks[11], landmarks[23], landmarks[25]),
                    'spine_angle': self._angle_between_points(landmarks[0], landmarks[11], landmarks[23])
                })
                
            elif self.exercise_type == ExerciseType.PLANK:
                angles.update({
                    'body_line': self._angle_between_points(landmarks[11], landmarks[23], landmarks[27]),
                    'hip_sag': self._calculate_hip_sag(landmarks)
                })
                
        except (IndexError, KeyError, TypeError):
            pass
            
        return angles
    
    def _angle_between_points(self, p1: Dict, p2: Dict, p3: Dict) -> float:
        """Calculate angle between three points"""
        try:
            a = np.array([p1['x'], p1['y']])
            b = np.array([p2['x'], p2['y']])
            c = np.array([p3['x'], p3['y']])
            
            ba = a - b
            bc = c - b
            
            cosine_angle = np.dot(ba, bc) / (np.linalg.norm(ba) * np.linalg.norm(bc))
            cosine_angle = np.clip(cosine_angle, -1.0, 1.0)
            angle = np.arccos(cosine_angle)
            
            return np.degrees(angle)
        except:
            return 0.0
    
    def _calculate_hip_sag(self, landmarks: List[Dict]) -> float:
        """Calculate hip sag for plank analysis"""
        try:
            # Calculate deviation from straight line
            shoulder = np.array([landmarks[11]['x'], landmarks[11]['y']])
            hip = np.array([landmarks[23]['x'], landmarks[23]['y']])
            ankle = np.array([landmarks[27]['x'], landmarks[27]['y']])
            
            # Calculate expected hip position on straight line
            line_vector = ankle - shoulder
            shoulder_to_hip = hip - shoulder
            
            # Project hip onto line
            projection_length = np.dot(shoulder_to_hip, line_vector) / np.linalg.norm(line_vector)
            projection = shoulder + (projection_length / np.linalg.norm(line_vector)) * line_vector
            
            # Calculate perpendicular distance (sag)
            sag_distance = np.linalg.norm(hip - projection)
            
            return sag_distance
        except:
            return 0.0
    
    def _calculate_velocity(self) -> Dict:
        """Calculate movement velocities"""
        if len(self.position_buffer) < 2:
            return {}
            
        current = self.position_buffer[-1]
        previous = self.position_buffer[-2]
        
        dt = current['timestamp'] - previous['timestamp']
        if dt == 0:
            return {}
            
        velocities = {}
        for joint, pos in current['positions'].items():
            if joint in previous['positions'] and pos and previous['positions'][joint]:
                dx = pos['x'] - previous['positions'][joint]['x']
                dy = pos['y'] - previous['positions'][joint]['y']
                velocities[joint] = math.sqrt(dx*dx + dy*dy) / dt
                
        return velocities
    
    def _calculate_acceleration(self) -> Dict:
        """Calculate movement accelerations"""
        if len(self.velocity_buffer) < 2:
            return {}
            
        current = self.velocity_buffer[-1]
        previous = self.velocity_buffer[-2]
        
        dt = current['timestamp'] - previous['timestamp']
        if dt == 0:
            return {}
            
        accelerations = {}
        for joint in current['velocity']:
            if joint in previous['velocity']:
                dv = current['velocity'][joint] - previous['velocity'][joint]
                accelerations[joint] = dv / dt
                
        return accelerations
    
    def _analyze_exercise_specific_form(self, landmarks: List[Dict]):
        """Perform exercise-specific form analysis"""
        if self.exercise_type == ExerciseType.PUSHUP:
            self._analyze_pushup_form(landmarks)
        elif self.exercise_type == ExerciseType.SQUAT:
            self._analyze_squat_form(landmarks)
        elif self.exercise_type == ExerciseType.SITUP:
            self._analyze_situp_form(landmarks)
        elif self.exercise_type == ExerciseType.PLANK:
            self._analyze_plank_form(landmarks)
        elif self.exercise_type == ExerciseType.BURPEE:
            self._analyze_burpee_form(landmarks)
        elif self.exercise_type == ExerciseType.VERTICAL_JUMP:
            self._analyze_jump_form(landmarks)
    
    def _analyze_pushup_form(self, landmarks: List[Dict]):
        """Analyze push-up specific form violations"""
        if len(self.angle_buffer) == 0:
            return
            
        current_angles = self.angle_buffer[-1]['angles']
        params = self.exercise_params['critical_angles']
        
        # Check elbow range of motion
        if 'left_elbow' in current_angles and 'right_elbow' in current_angles:
            avg_elbow = (current_angles['left_elbow'] + current_angles['right_elbow']) / 2
            
            if avg_elbow > params['elbow_max'] - 10:  # Near full extension
                if len(self.angle_buffer) > 5:
                    min_angle = min(angles['angles'].get('left_elbow', 180) + 
                                  angles['angles'].get('right_elbow', 180) for angles in list(self.angle_buffer)[-5:]) / 2
                    rom = avg_elbow - min_angle
                    
                    if rom < self.exercise_params['min_range_of_motion']:
                        self.current_violations.add(ViolationType.PARTIAL_ROM)
        
        # Check body alignment
        if 'body_line' in current_angles:
            if (current_angles['body_line'] < params['body_line_min'] or 
                current_angles['body_line'] > params['body_line_max']):
                self.current_violations.add(ViolationType.BODY_ALIGNMENT)
    
    def _analyze_squat_form(self, landmarks: List[Dict]):
        """Analyze squat specific form violations"""
        if len(self.angle_buffer) == 0:
            return
            
        current_angles = self.angle_buffer[-1]['angles']
        params = self.exercise_params['critical_angles']
        
        # Check knee tracking and depth
        if 'left_knee' in current_angles and 'right_knee' in current_angles:
            avg_knee = (current_angles['left_knee'] + current_angles['right_knee']) / 2
            
            # Check for partial squats
            if avg_knee > params['knee_min'] + 20:
                self.current_violations.add(ViolationType.PARTIAL_ROM)
        
        # Check hip hinge pattern
        if 'left_hip' in current_angles and 'right_hip' in current_angles:
            avg_hip = (current_angles['left_hip'] + current_angles['right_hip']) / 2
            
            if avg_hip < params['hip_min'] or avg_hip > params['hip_max']:
                self.current_violations.add(ViolationType.POOR_FORM)
    
    def _analyze_situp_form(self, landmarks: List[Dict]):
        """Analyze sit-up specific form violations"""
        if len(self.angle_buffer) == 0:
            return
            
        current_angles = self.angle_buffer[-1]['angles']
        params = self.exercise_params['critical_angles']
        
        # Check hip angle for proper sit-up motion
        if 'hip_angle' in current_angles:
            if (current_angles['hip_angle'] < params['hip_min'] or 
                current_angles['hip_angle'] > params['hip_max']):
                self.current_violations.add(ViolationType.PARTIAL_ROM)
    
    def _analyze_plank_form(self, landmarks: List[Dict]):
        """Analyze plank specific form violations"""
        if len(self.angle_buffer) == 0:
            return
            
        current_angles = self.angle_buffer[-1]['angles']
        params = self.exercise_params['critical_angles']
        
        # Check body line alignment
        if 'body_line' in current_angles:
            if (current_angles['body_line'] < params['body_line_min'] or 
                current_angles['body_line'] > params['body_line_max']):
                self.current_violations.add(ViolationType.BODY_ALIGNMENT)
        
        # Check for hip sag
        if 'hip_sag' in current_angles:
            if current_angles['hip_sag'] > params['hip_sag_max']:
                self.current_violations.add(ViolationType.POOR_FORM)
    
    def _analyze_burpee_form(self, landmarks: List[Dict]):
        """Analyze burpee specific form violations"""
        # Combine pushup and squat analysis
        self._analyze_pushup_form(landmarks)
        self._analyze_squat_form(landmarks)
    
    def _analyze_jump_form(self, landmarks: List[Dict]):
        """Analyze vertical jump form violations"""
        if len(self.position_buffer) < 5:
            return
            
        # Analyze jump height consistency
        recent_positions = list(self.position_buffer)[-5:]
        hip_heights = [pos['positions'].get('left_hip', {}).get('y', 0) for pos in recent_positions]
        
        if len(set(hip_heights)) > 3:  # Too much variation
            jump_range = max(hip_heights) - min(hip_heights)
            if jump_range < self.exercise_params['min_range_of_motion']:
                self.current_violations.add(ViolationType.PARTIAL_ROM)
    
    def _analyze_movement_patterns(self):
        """Analyze movement patterns for violations"""
        if len(self.velocity_buffer) < 5:
            return
            
        # Check for bouncing (rapid velocity changes)
        recent_velocities = list(self.velocity_buffer)[-5:]
        for joint in ['left_knee', 'right_knee', 'left_elbow', 'right_elbow']:
            velocities = [v['velocity'].get(joint, 0) for v in recent_velocities if joint in v['velocity']]
            
            if len(velocities) >= 3:
                # Check for rapid direction changes (bouncing)
                direction_changes = 0
                for i in range(1, len(velocities) - 1):
                    if ((velocities[i-1] < velocities[i] > velocities[i+1]) or 
                        (velocities[i-1] > velocities[i] < velocities[i+1])):
                        direction_changes += 1
                
                if direction_changes >= 2:
                    self.current_violations.add(ViolationType.BOUNCING)
    
    def _analyze_timing_patterns(self, timestamp: float):
        """Analyze timing patterns for violations"""
        if not self.rep_start_time:
            self.rep_start_time = timestamp
            return
            
        rep_duration = timestamp - self.rep_start_time
        params = self.exercise_params
        
        # Check rep timing
        if rep_duration < params['min_rep_time']:
            self.current_violations.add(ViolationType.SPEED_VIOLATION)
        elif rep_duration > params['max_rep_time']:
            self.current_violations.add(ViolationType.INCONSISTENT_TIMING)
    
    def _calculate_form_quality(self) -> float:
        """Calculate overall form quality score (0-100)"""
        base_score = 100.0
        
        # Deduct points for current violations
        violation_penalties = {
            ViolationType.PARTIAL_ROM: 20,
            ViolationType.POOR_FORM: 15,
            ViolationType.BOUNCING: 10,
            ViolationType.BODY_ALIGNMENT: 15,
            ViolationType.SPEED_VIOLATION: 10,
            ViolationType.INCONSISTENT_TIMING: 8,
            ViolationType.INCOMPLETE_REP: 25,
            ViolationType.ASSISTANCE: 30
        }
        
        for violation in self.current_violations:
            penalty = violation_penalties.get(violation, 10)
            base_score -= penalty * self.strictness
        
        return max(0.0, min(100.0, base_score))
    
    def _calculate_confidence(self) -> float:
        """Calculate confidence in the analysis (0-1)"""
        confidence = 1.0
        
        # Reduce confidence if insufficient data
        if len(self.position_buffer) < 10:
            confidence *= 0.5
        
        if len(self.angle_buffer) < 5:
            confidence *= 0.7
        
        # Reduce confidence for poor landmark visibility
        if len(self.angle_buffer) > 0:
            recent_angles = self.angle_buffer[-1]['angles']
            if len(recent_angles) < 3:
                confidence *= 0.6
        
        return confidence
    
    def _get_analysis_result(self, form_quality: float = 0.0, confidence: float = 0.0) -> Dict:
        """Get formatted analysis result"""
        return {
            'violations': list(self.current_violations),
            'violation_counts': dict(self.violations),
            'form_quality': form_quality,
            'confidence': confidence,
            'overall_score': form_quality * confidence,
            'recommendations': self._get_recommendations(),
            'violation_severity': self._calculate_severity()
        }
    
    def _get_recommendations(self) -> List[str]:
        """Get form improvement recommendations"""
        recommendations = []
        
        violation_advice = {
            ViolationType.PARTIAL_ROM: "Increase your range of motion - go deeper/lower",
            ViolationType.POOR_FORM: "Focus on proper form and technique",
            ViolationType.BOUNCING: "Control your movement - avoid bouncing",
            ViolationType.BODY_ALIGNMENT: "Keep your body in proper alignment",
            ViolationType.SPEED_VIOLATION: "Slow down your movement for better control",
            ViolationType.INCONSISTENT_TIMING: "Maintain consistent timing between reps",
            ViolationType.INCOMPLETE_REP: "Complete the full repetition",
            ViolationType.ASSISTANCE: "Perform the exercise without external assistance"
        }
        
        for violation in self.current_violations:
            if violation in violation_advice:
                recommendations.append(violation_advice[violation])
        
        return recommendations
    
    def _calculate_severity(self) -> str:
        """Calculate violation severity level"""
        if not self.current_violations:
            return "none"
        
        severe_violations = {ViolationType.ASSISTANCE, ViolationType.INCOMPLETE_REP}
        moderate_violations = {ViolationType.PARTIAL_ROM, ViolationType.POOR_FORM, ViolationType.BODY_ALIGNMENT}
        
        if any(v in severe_violations for v in self.current_violations):
            return "severe"
        elif any(v in moderate_violations for v in self.current_violations):
            return "moderate"
        else:
            return "minor"
    
    def reset_rep(self):
        """Reset for new repetition"""
        self.rep_start_time = None
        self.current_rep_violations.clear()
    
    def get_session_summary(self) -> Dict:
        """Get comprehensive session summary"""
        total_violations = sum(self.violations.values())
        avg_form_quality = np.mean(self.form_quality_history) if self.form_quality_history else 0
        
        return {
            'total_violations': total_violations,
            'violation_breakdown': dict(self.violations),
            'average_form_quality': avg_form_quality,
            'form_consistency': 1.0 / (1.0 + np.std(self.form_quality_history)) if len(self.form_quality_history) > 1 else 1.0,
            'average_confidence': np.mean(self.confidence_scores) if self.confidence_scores else 0,
            'exercise_type': self.exercise_type.value,
            'strictness_level': self.strictness
        }

# Example usage and testing
if __name__ == "__main__":
    # Test cheat detection system
    exercises = [ExerciseType.PUSHUP, ExerciseType.SQUAT]
    
    for exercise in exercises:
        print(f"\nTesting {exercise.value} cheat detection:")
        detector = CheatDetectionSystem(exercise, strictness=0.8)
        
        # Simulate some test data with violations
        for i in range(10):
            # Create mock landmarks
            mock_landmarks = [{'x': 0.5, 'y': 0.5, 'z': 0, 'visibility': 0.9} for _ in range(33)]
            
            # Simulate poor form (partial range of motion)
            if i % 3 == 0:
                # Simulate partial ROM
                mock_landmarks[13]['x'] = 0.4  # Elbow position
                mock_landmarks[14]['x'] = 0.6
            
            result = detector.analyze_form(mock_landmarks, time.time() + i * 0.1)
            
            if result['violations']:
                print(f"Frame {i}: Violations detected - {[v.value for v in result['violations']]}")
                print(f"  Form quality: {result['form_quality']:.1f}")
                print(f"  Recommendations: {result['recommendations']}")
        
        # Get session summary
        summary = detector.get_session_summary()
        print(f"\nSession Summary:")
        print(f"  Total violations: {summary['total_violations']}")
        print(f"  Average form quality: {summary['average_form_quality']:.1f}")
        print(f"  Form consistency: {summary['form_consistency']:.2f}")
        print("-" * 60)

