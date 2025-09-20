"""
SIH Sports Assessment Platform - Main Demo
Complete integration of all systems for demonstration
"""

import cv2
import time
import sys
import os
import json
import threading
import numpy as np
from typing import Dict, List, Optional
from enum import Enum

# Add paths for imports
current_dir = os.path.dirname(os.path.abspath(__file__))
wow_dir = os.path.join(os.path.dirname(current_dir), 'WOW-Features')
sys.path.append(current_dir)
sys.path.append(wow_dir)

# Core system imports
from pose import PoseDetector
from improved_rep_counter import ImprovedRepCounter, ExerciseType
from cheat_detection_system import CheatDetectionSystem, ViolationType
from accurate_rep_counter import AccurateRepCounter

# WOW Features imports
try:
    from integrated_wow_system import IntegratedWOWSystem, Language
except ImportError:
    print("WOW Features not available - running in basic mode")
    IntegratedWOWSystem = None
    Language = None

class DemoMode(Enum):
    BASIC = "basic"
    WOW_FEATURES = "wow_features"
    FULL_ASSESSMENT = "full_assessment"

class SportsAssessmentDemo:
    """
    Complete Sports Assessment Platform Demo
    """
    
    def __init__(self, mode: DemoMode = DemoMode.FULL_ASSESSMENT):
        self.mode = mode
        self.is_running = False
        
        # Core components
        self.pose_detector = PoseDetector()
        self.rep_counter = None
        self.cheat_detector = None
        self.current_exercise = None
        
        # WOW Features
        self.wow_system = None
        if IntegratedWOWSystem and mode in [DemoMode.WOW_FEATURES, DemoMode.FULL_ASSESSMENT]:
            try:
                self.wow_system = IntegratedWOWSystem(Language.ENGLISH)
                print("🚀 WOW Features enabled!")
            except Exception as e:
                print(f"WOW Features initialization failed: {e}")
        
        # Demo state
        self.session_data = {
            'start_time': None,
            'total_reps': 0,
            'form_scores': [],
            'violations': [],
            'landmarks_history': []
        }
        
        # UI state
        self.show_metrics = True
        self.show_pose = True
        self.recording = False
        
        # Mobile-friendly portrait canvas dimensions
        self.canvas_width = 360
        self.canvas_height = 640
        
    def start_demo(self, exercise_type: str = "pushup"):
        """Start the sports assessment demo"""
        print(f"\n🏋️ Starting SIH Sports Assessment Demo")
        print(f"Exercise: {exercise_type.upper()}")
        print(f"Mode: {self.mode.value.upper()}")
        print("=" * 60)
        
        # Initialize for specific exercise
        self._setup_exercise(exercise_type)
        
        # Start WOW features session
        if self.wow_system:
            test_athlete = {
                'age': 24,
                'height': 175,
                'weight': 70,
                'gender': 'men',
                'training_years': 2
            }
            self.wow_system.start_workout_session(exercise_type, test_athlete)
        
        # Start camera demo
        self._run_camera_demo()
    
    def _setup_exercise(self, exercise_type: str):
        """Setup exercise-specific components"""
        try:
            exercise_enum = ExerciseType(exercise_type.lower())
        except ValueError:
            exercise_enum = ExerciseType.PUSHUP
            print(f"Unknown exercise '{exercise_type}', defaulting to pushup")
        
        self.current_exercise = exercise_enum
        
        # Initialize accurate rep counter (prevents false positives)
        exercise_name = exercise_enum.value if hasattr(exercise_enum, 'value') else str(exercise_enum)
        self.rep_counter = AccurateRepCounter(exercise_name)
        
        # Keep old rep counter as backup
        self.legacy_rep_counter = ImprovedRepCounter(exercise_enum)
        
        # Initialize cheat detector
        self.cheat_detector = CheatDetectionSystem(exercise_enum, strictness=0.7)
        
        print(f"✅ Initialized {exercise_enum.value} assessment")
    
    def _run_camera_demo(self):
        """Run the main camera-based demo"""
        cap = cv2.VideoCapture(0)
        
        if not cap.isOpened():
            print("❌ Error: Could not open camera")
            return
        
        # Set higher resolution for better display
        cap.set(cv2.CAP_PROP_FRAME_WIDTH, 1280)
        cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 720)
        cap.set(cv2.CAP_PROP_FPS, 30)
        
        print("\n📹 Camera demo started")
        print("Controls:")
        print("  SPACE - Start assessment (press again to stop)")
        print("  'r' - Reset counters")
        print("  'm' - Toggle metrics display")
        print("  'p' - Toggle pose display")
        print("  'f' - Toggle fullscreen mode")
        print("  'ESC' - Exit fullscreen")
        print("  'q' - Quit demo")
        
        self.is_running = False
        self.session_data['start_time'] = time.time()
        
        # Display settings
        self.fullscreen_mode = False
        self.window_name = 'SIH Sports Assessment Platform'
        
        # Create window with resizable flag
        cv2.namedWindow(self.window_name, cv2.WINDOW_NORMAL)
        cv2.resizeWindow(self.window_name, self.canvas_width, self.canvas_height)
        
        try:
            while True:
                ret, frame = cap.read()
                if not ret:
                    break
                
                # Process frame
                processed_frame = self._process_frame(frame)
                
                # Display frame rendered to mobile portrait canvas
                mobile_canvas = self._compose_mobile_canvas(processed_frame)
                cv2.imshow(self.window_name, mobile_canvas)
                
                # Handle key presses
                key = cv2.waitKey(1) & 0xFF
                if key == ord('q'):
                    break
                elif key == 27:  # ESC key - Exit fullscreen
                    if self.fullscreen_mode:
                        self.fullscreen_mode = False
                        cv2.setWindowProperty(self.window_name, cv2.WND_PROP_FULLSCREEN, cv2.WINDOW_NORMAL)
                        cv2.resizeWindow(self.window_name, self.canvas_width, self.canvas_height)
                        print("🔳 Exited fullscreen mode")
                elif key == ord('f'):  # Toggle fullscreen
                    self.fullscreen_mode = not self.fullscreen_mode
                    if self.fullscreen_mode:
                        cv2.setWindowProperty(self.window_name, cv2.WND_PROP_FULLSCREEN, cv2.WINDOW_FULLSCREEN)
                        print("🔲 Entered fullscreen mode")
                    else:
                        cv2.setWindowProperty(self.window_name, cv2.WND_PROP_FULLSCREEN, cv2.WINDOW_NORMAL)
                        cv2.resizeWindow(self.window_name, self.canvas_width, self.canvas_height)
                        print("🔳 Exited fullscreen mode")
                elif key == ord(' '):  # Space to start/stop
                    if not self.is_running:
                        self.is_running = True
                        print("🟢 Assessment STARTED")
                        self.session_data['start_time'] = time.time()
                    else:
                        self.is_running = False
                        print("🔴 Assessment STOPPED")
                        self._show_session_summary()
                elif key == ord('r'):  # Reset
                    self._reset_session()
                    print("🔄 Session RESET")
                elif key == ord('m'):  # Toggle metrics
                    self.show_metrics = not self.show_metrics
                    print(f"📊 Metrics display: {'ON' if self.show_metrics else 'OFF'}")
                elif key == ord('p'):  # Toggle pose
                    self.show_pose = not self.show_pose
                    print(f"🤖 Pose overlay: {'ON' if self.show_pose else 'OFF'}")
                
        except KeyboardInterrupt:
            print("\n⏹️ Demo stopped by user")
        finally:
            cap.release()
            cv2.destroyAllWindows()
            self._cleanup()
    
    def _process_frame(self, frame) -> cv2.Mat:
        """Process single frame with all assessments"""
        # Detect pose
        processed_frame, landmarks = self.pose_detector.detect_pose(
            frame, draw=self.show_pose
        )
        
        if not self.is_running or not landmarks:
            self._draw_status(processed_frame, "PAUSED - Press SPACE to start")
            return processed_frame
        
        # Store landmarks
        self.session_data['landmarks_history'].append(landmarks)
        
        # Get raw MediaPipe landmarks for rep counter
        raw_landmarks = self.pose_detector.get_raw_landmarks(frame)
        
        # Update exercise-specific assessment
        if raw_landmarks:
            if self.current_exercise == ExerciseType.PLANK:
                # Plank is time-based, not rep-based
                rep_result = self._assess_plank_hold(raw_landmarks)
            else:
                # Other exercises use rep counting
                rep_result = self.rep_counter.update_rep_count(raw_landmarks)
        else:
            if self.current_exercise == ExerciseType.PLANK:
                rep_result = {'hold_time': 0, 'current_state': 'NONE', 'confidence': 0.0}
            else:
                rep_result = {'rep_count': self.rep_counter.rep_count, 'current_state': 'NONE', 'confidence': 0.0}
        
        # Get current metric based on exercise type
        if self.current_exercise == ExerciseType.PLANK:
            current_metric = rep_result.get('hold_time', 0)
        else:
            current_metric = rep_result.get('rep_count', 0)
        
        # Update cheat detector
        cheat_result = self.cheat_detector.analyze_form(landmarks)
        form_quality = cheat_result['form_quality']
        violations = cheat_result['violations']
        
        # Store session data
        if self.current_exercise == ExerciseType.PLANK:
            self.session_data['total_hold_time'] = current_metric
        else:
            self.session_data['total_reps'] = current_metric
        self.session_data['form_scores'].append(form_quality)
        if violations:
            self.session_data['violations'].extend([v.value for v in violations])
        
        # Update WOW features
        if self.wow_system:
            try:
                # Use appropriate metric based on exercise type
                if self.current_exercise == ExerciseType.PLANK:
                    # For plank, convert hold time to "reps" for WOW system compatibility
                    # 1 second = 0.1 "reps" for coaching purposes
                    wow_reps = current_metric * 0.1
                    wow_response = self.wow_system.update_workout_progress(
                        rep_count=wow_reps,
                        form_quality=form_quality,
                        violations=[v.value for v in violations],
                        landmarks=landmarks
                    )
                else:
                    wow_response = self.wow_system.update_workout_progress(
                        rep_count=current_metric,
                        form_quality=form_quality,
                        violations=[v.value for v in violations],
                        landmarks=landmarks
                    )
                
                # Display WOW coaching messages
                if 'voice_coaching' in wow_response:
                    coaching = wow_response['voice_coaching']
                    if coaching.get('coaching_given'):
                        for coach_msg in coaching['coaching_given']:
                            print(f"🎤 Coach: {coach_msg['message']}")
                
            except Exception as e:
                print(f"WOW Features error: {e}")
        
        # Draw all metrics
        if self.show_metrics:
            self._draw_metrics(processed_frame, rep_result, cheat_result)
        
        return processed_frame

    def _compose_mobile_canvas(self, frame: cv2.Mat) -> cv2.Mat:
        """Render the given frame into a portrait 360x640 canvas with aspect-fit padding."""
        try:
            target_w, target_h = self.canvas_width, self.canvas_height
            h, w = frame.shape[:2]
            if h == 0 or w == 0:
                return cv2.resize(frame, (target_w, target_h))

            # Compute aspect-fit size
            scale = min(target_w / w, target_h / h)
            new_w = max(1, int(w * scale))
            new_h = max(1, int(h * scale))
            resized = cv2.resize(frame, (new_w, new_h))

            # Create black portrait canvas
            canvas = np.zeros((target_h, target_w, 3), dtype=np.uint8)

            # Center the resized frame
            x_offset = (target_w - new_w) // 2
            y_offset = (target_h - new_h) // 2
            canvas[y_offset:y_offset + new_h, x_offset:x_offset + new_w] = resized
            return canvas
        except Exception:
            # Fallback to simple resize
            return cv2.resize(frame, (self.canvas_width, self.canvas_height))
    
    def _assess_plank_hold(self, landmarks):
        """Assess plank hold time and stability"""
        current_time = time.time()
        
        # Initialize plank session if not exists
        if not hasattr(self, 'plank_start_time'):
            self.plank_start_time = None
            self.plank_hold_time = 0
            self.plank_stable_count = 0
        
        # Check if in proper plank position
        is_stable = self._check_plank_stability(landmarks)
        
        if is_stable:
            if self.plank_start_time is None:
                self.plank_start_time = current_time
                self.plank_stable_count = 0
            else:
                self.plank_hold_time = current_time - self.plank_start_time
                self.plank_stable_count += 1
        else:
            # Reset if not stable
            if self.plank_stable_count > 0:
                self.plank_stable_count -= 1
            if self.plank_stable_count <= 0:
                self.plank_start_time = None
                self.plank_hold_time = 0
        
        # Determine state
        if self.plank_hold_time > 0:
            state = "HOLDING"
        elif is_stable:
            state = "GETTING_READY"
        else:
            state = "ADJUST_POSITION"
        
        return {
            'hold_time': self.plank_hold_time,
            'current_state': state,
            'confidence': min(1.0, self.plank_stable_count / 10.0),
            'is_stable': is_stable
        }
    
    def _check_plank_stability(self, landmarks):
        """Check if body is in stable plank position"""
        try:
            # Get key points for plank assessment
            left_shoulder = landmarks.landmark[11]  # LEFT_SHOULDER
            right_shoulder = landmarks.landmark[12]  # RIGHT_SHOULDER
            left_hip = landmarks.landmark[23]  # LEFT_HIP
            right_hip = landmarks.landmark[24]  # RIGHT_HIP
            left_ankle = landmarks.landmark[27]  # LEFT_ANKLE
            right_ankle = landmarks.landmark[28]  # RIGHT_ANKLE
            
            # Check visibility
            key_points = [left_shoulder, right_shoulder, left_hip, right_hip, left_ankle, right_ankle]
            if any(point.visibility < 0.7 for point in key_points):
                return False
            
            # Check if body is roughly horizontal (plank position)
            # Shoulder-hip-ankle should be roughly in line
            shoulder_hip_angle = self._calculate_angle(left_shoulder, left_hip, left_ankle)
            
            # Plank angle should be close to 180 degrees (straight line)
            angle_tolerance = 20  # degrees
            is_horizontal = abs(shoulder_hip_angle - 180) < angle_tolerance
            
            # Check if hips are not too high or too low
            shoulder_y = (left_shoulder.y + right_shoulder.y) / 2
            hip_y = (left_hip.y + right_hip.y) / 2
            ankle_y = (left_ankle.y + right_ankle.y) / 2
            
            # Hips should be between shoulders and ankles
            hips_in_position = shoulder_y < hip_y < ankle_y
            
            return is_horizontal and hips_in_position
            
        except Exception as e:
            return False
    
    def _calculate_angle(self, point1, point2, point3):
        """Calculate angle between three points"""
        try:
            # Convert to numpy arrays
            p1 = np.array([point1.x, point1.y])
            p2 = np.array([point2.x, point2.y])
            p3 = np.array([point3.x, point3.y])
            
            # Calculate vectors
            v1 = p1 - p2
            v2 = p3 - p2
            
            # Calculate angle
            cos_angle = np.dot(v1, v2) / (np.linalg.norm(v1) * np.linalg.norm(v2))
            cos_angle = np.clip(cos_angle, -1.0, 1.0)
            angle = np.arccos(cos_angle)
            
            return np.degrees(angle)
        except:
            return 0
    
    def _draw_metrics(self, frame, rep_result: Dict, cheat_result: Dict):
        """Draw all assessment metrics on frame"""
        height, width = frame.shape[:2]
        
        # Scale metrics panel based on frame size
        panel_width = min(500, width // 3)
        panel_height = min(300, height // 3)
        
        # Background for metrics
        overlay = frame.copy()
        cv2.rectangle(overlay, (15, 15), (panel_width, panel_height), (0, 0, 0), -1)
        cv2.addWeighted(overlay, 0.8, frame, 0.2, 0, frame)
        
        # Add border
        cv2.rectangle(frame, (15, 15), (panel_width, panel_height), (0, 255, 255), 2)
        
        # Scale font sizes based on frame size
        font_scale_large = max(0.8, width / 1600)
        font_scale_medium = max(0.6, width / 2000)
        font_scale_small = max(0.5, width / 2400)
        
        # Title
        cv2.putText(frame, 'SIH SPORTS ASSESSMENT', (25, 50), 
                   cv2.FONT_HERSHEY_SIMPLEX, font_scale_large, (0, 255, 255), 2)
        
        # Exercise type
        exercise_name = self.current_exercise.value.upper()
        cv2.putText(frame, f'Exercise: {exercise_name}', (25, 80), 
                   cv2.FONT_HERSHEY_SIMPLEX, font_scale_small, (255, 255, 255), 1)
        
        # Exercise-specific metric - Large and prominent
        if self.current_exercise == ExerciseType.PLANK:
            hold_time = rep_result.get('hold_time', 0)
            time_text = f"{hold_time:.1f}s"
            cv2.putText(frame, f'HOLD TIME: {time_text}', (25, 120), 
                       cv2.FONT_HERSHEY_SIMPLEX, font_scale_large, (0, 255, 0), 3)
        else:
            reps = rep_result.get('rep_count', 0)
            cv2.putText(frame, f'REPS: {reps}', (25, 120), 
                       cv2.FONT_HERSHEY_SIMPLEX, font_scale_large, (0, 255, 0), 3)
        
        # Form quality
        form_quality = cheat_result['form_quality']
        color = (0, 255, 0) if form_quality >= 80 else (0, 255, 255) if form_quality >= 60 else (0, 0, 255)
        cv2.putText(frame, f'Form: {form_quality:.1f}%', (25, 160), 
                   cv2.FONT_HERSHEY_SIMPLEX, font_scale_medium, color, 2)
        
        # Current state
        state = rep_result['current_state']
        cv2.putText(frame, f'State: {state}', (25, 190), 
                   cv2.FONT_HERSHEY_SIMPLEX, font_scale_small, (255, 255, 255), 1)
        
        # Display confidence (new feature)
        confidence = rep_result.get('confidence', 0.0)
        confidence_color = (0, 255, 0) if confidence > 0.7 else (0, 0, 255)
        cv2.putText(frame, f'Confidence: {confidence:.1f}', (25, 220), 
                   cv2.FONT_HERSHEY_SIMPLEX, font_scale_small, confidence_color, 1)
        
        # Violations
        violations = cheat_result['violations']
        if violations:
            violation_text = f'Issues: {len(violations)}'
            cv2.putText(frame, violation_text, (20, 160), 
                       cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 1)
        else:
            cv2.putText(frame, 'Form: GOOD', (20, 160), 
                       cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 1)
        
        # FPS
        fps = self.pose_detector.get_fps()
        cv2.putText(frame, f'FPS: {fps}', (25, 250), 
                   cv2.FONT_HERSHEY_SIMPLEX, font_scale_small, (255, 255, 255), 1)
        
        # Assessment status indicator
        status_color = (0, 255, 0) if self.is_running else (0, 0, 255)
        status_text = "🟢 RUNNING" if self.is_running else "🔴 STOPPED"
        cv2.putText(frame, status_text, (25, 280), 
                   cv2.FONT_HERSHEY_SIMPLEX, font_scale_medium, status_color, 2)
        
        # Display mode indicator
        mode_text = "FULLSCREEN" if self.fullscreen_mode else f"WINDOWED ({width}x{height})"
        cv2.putText(frame, mode_text, (25, 310), 
                   cv2.FONT_HERSHEY_SIMPLEX, font_scale_small, (255, 255, 0), 1)
        
        # Session timer
        if self.session_data['start_time']:
            elapsed = time.time() - self.session_data['start_time']
            minutes = int(elapsed // 60)
            seconds = int(elapsed % 60)
            cv2.putText(frame, f'Time: {minutes:02d}:{seconds:02d}', (300, 85), 
                       cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255, 255, 0), 2)
        
        # WOW Features indicator
        if self.wow_system:
            cv2.putText(frame, 'WOW AI: ON', (300, 110), 
                       cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 255), 1)
        
        # Right side - Enhanced performance bar
        self._draw_performance_bar(frame, form_quality, width, height)
    
    def _draw_performance_bar(self, frame, form_quality: float, width: int, height: int):
        """Draw enhanced performance quality bar"""
        # Scale bar based on frame size
        bar_width = max(40, width // 30)
        bar_height = max(200, height // 4)
        bar_x = width - bar_width - 20
        bar_y = 50
        
        # Performance bar background with border
        cv2.rectangle(frame, (bar_x - 2, bar_y - 2), (bar_x + bar_width + 2, bar_y + bar_height + 2), (255, 255, 255), 2)
        cv2.rectangle(frame, (bar_x, bar_y), (bar_x + bar_width, bar_y + bar_height), (50, 50, 50), -1)
        
        # Performance level
        fill_height = int((form_quality / 100) * bar_height)
        fill_y = bar_y + bar_height - fill_height
        
        # Color based on performance with gradient effect
        if form_quality >= 80:
            color = (0, 255, 0)  # Green
            status = "EXCELLENT"
        elif form_quality >= 60:
            color = (0, 255, 255)  # Yellow
            status = "GOOD"
        else:
            color = (0, 0, 255)  # Red
            status = "NEEDS WORK"
        
        # Draw filled portion
        cv2.rectangle(frame, (bar_x, fill_y), (bar_x + bar_width, bar_y + bar_height), color, -1)
        
        # Add performance markers
        for i in range(0, 101, 20):
            marker_y = bar_y + bar_height - int((i / 100) * bar_height)
            cv2.line(frame, (bar_x - 5, marker_y), (bar_x, marker_y), (255, 255, 255), 1)
            if i % 40 == 0:  # Show labels for 0, 40, 80
                font_scale = max(0.3, width / 3000)
                cv2.putText(frame, f'{i}', (bar_x - 25, marker_y + 5), 
                           cv2.FONT_HERSHEY_SIMPLEX, font_scale, (255, 255, 255), 1)
        
        # Performance text
        font_scale = max(0.5, width / 2400)
        cv2.putText(frame, f'{form_quality:.0f}%', (bar_x - 20, bar_y + bar_height + 25), 
                   cv2.FONT_HERSHEY_SIMPLEX, font_scale, color, 2)
        cv2.putText(frame, status, (bar_x - 30, bar_y + bar_height + 50), 
                   cv2.FONT_HERSHEY_SIMPLEX, font_scale * 0.7, color, 1)
        
        # Title for the bar
        cv2.putText(frame, 'FORM QUALITY', (bar_x - 20, bar_y - 10), 
                   cv2.FONT_HERSHEY_SIMPLEX, font_scale * 0.6, (255, 255, 255), 1)
    
    def _draw_status(self, frame, status: str):
        """Draw status message"""
        height, width = frame.shape[:2]
        
        # Status background
        text_size = cv2.getTextSize(status, cv2.FONT_HERSHEY_SIMPLEX, 1, 2)[0]
        text_x = (width - text_size[0]) // 2
        text_y = height // 2
        
        cv2.rectangle(frame, (text_x - 20, text_y - 40), 
                     (text_x + text_size[0] + 20, text_y + 20), (0, 0, 0), -1)
        
        cv2.putText(frame, status, (text_x, text_y), 
                   cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 255), 2)
    
    def _reset_session(self):
        """Reset current session"""
        if self.rep_counter:
            self.rep_counter.reset()
        
        if self.wow_system:
            self.wow_system.current_session = None
        
        self.session_data = {
            'start_time': time.time(),
            'total_reps': 0,
            'form_scores': [],
            'violations': [],
            'landmarks_history': []
        }
    
    def _show_session_summary(self):
        """Display session summary"""
        if not self.session_data['start_time']:
            return
        
        duration = time.time() - self.session_data['start_time']
        avg_form = (sum(self.session_data['form_scores']) / len(self.session_data['form_scores']) 
                   if self.session_data['form_scores'] else 0)
        
        print(f"\n📊 SESSION SUMMARY")
        print(f"{'='*40}")
        print(f"Exercise: {self.current_exercise.value.upper()}")
        print(f"Duration: {duration/60:.1f} minutes")
        
        if self.current_exercise == ExerciseType.PLANK:
            print(f"Total Hold Time: {self.session_data.get('total_hold_time', 0):.1f} seconds")
        else:
            print(f"Total Reps: {self.session_data.get('total_reps', 0)}")
            
        print(f"Average Form: {avg_form:.1f}%")
        print(f"Total Violations: {len(self.session_data['violations'])}")
        print(f"{'='*40}")
        
        # WOW Features summary
        if self.wow_system and self.wow_system.current_session:
            try:
                wow_summary = self.wow_system.end_workout_session({
                    'age': 24, 'height': 175, 'weight': 70, 'gender': 'men', 'training_years': 2
                })
                
                if wow_summary.get('talent_analysis'):
                    talent = wow_summary['talent_analysis']['talent_prediction']
                    print(f"\n🏆 AI TALENT ANALYSIS:")
                    print(f"Talent Level: {talent['level'].upper()}")
                    print(f"Talent Score: {talent['score']:.1f}/100")
                    print(f"Percentile: {talent['percentile']:.1f}%")
                
                if wow_summary.get('recommendations'):
                    print(f"\n💡 AI RECOMMENDATIONS:")
                    for i, rec in enumerate(wow_summary['recommendations'][:3], 1):
                        print(f"{i}. {rec}")
                        
            except Exception as e:
                print(f"WOW summary error: {e}")
    
    def _cleanup(self):
        """Cleanup resources"""
        if self.pose_detector:
            self.pose_detector.cleanup()
        
        if self.wow_system:
            self.wow_system.cleanup()
        
        print("🧹 Demo cleanup completed")

def main():
    """Main demo function"""
    print("🏆 SIH SPORTS TALENT ASSESSMENT PLATFORM")
    print("🚀 Complete AI-Powered Sports Assessment Demo")
    print("=" * 60)
    
    # Available exercises
    exercises = ["pushup", "squat", "situp", "plank", "burpee"]
    
    print("\nAvailable Exercises:")
    for i, exercise in enumerate(exercises, 1):
        print(f"{i}. {exercise.title()}")
    
    # Get user choice
    try:
        choice = input(f"\nSelect exercise (1-{len(exercises)}) or press Enter for pushup: ").strip()
        if choice and choice.isdigit():
            exercise_idx = int(choice) - 1
            if 0 <= exercise_idx < len(exercises):
                selected_exercise = exercises[exercise_idx]
            else:
                selected_exercise = "pushup"
        else:
            selected_exercise = "pushup"
    except KeyboardInterrupt:
        print("\nDemo cancelled")
        return
    
    # Demo mode selection
    print(f"\nDemo Modes:")
    print(f"1. Basic Assessment (Pose + Rep Counting)")
    print(f"2. WOW Features (AI Coach + Talent Prediction)")
    print(f"3. Full Assessment (All Features)")
    
    try:
        mode_choice = input(f"\nSelect mode (1-3) or press Enter for Full: ").strip()
        if mode_choice == "1":
            demo_mode = DemoMode.BASIC
        elif mode_choice == "2":
            demo_mode = DemoMode.WOW_FEATURES
        else:
            demo_mode = DemoMode.FULL_ASSESSMENT
    except KeyboardInterrupt:
        print("\nDemo cancelled")
        return
    
    # Start demo
    try:
        demo = SportsAssessmentDemo(demo_mode)
        demo.start_demo(selected_exercise)
    except KeyboardInterrupt:
        print("\n⏹️ Demo interrupted")
    except Exception as e:
        print(f"❌ Demo error: {e}")
    finally:
        print("👋 Thank you for using SIH Sports Assessment Platform!")

if __name__ == "__main__":
    main()



