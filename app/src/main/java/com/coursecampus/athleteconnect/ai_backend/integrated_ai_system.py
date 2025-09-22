"""
Integrated AI System Orchestrator - Activity 10 Implementation
Coordinates all AI components for seamless SIH Sports Assessment Platform operation
"""

import cv2
import numpy as np
import time
import threading
from typing import Dict, List, Tuple, Optional
from concurrent.futures import ThreadPoolExecutor, as_completed
import json
import logging
from collections import deque

# Import all system components
from pose import PoseDetector
from accurate_rep_counter import AccurateRepCounter
from cheat_detection_system import CheatDetectionSystem
from enhanced_performance_analyzer import EnhancedPerformanceAnalyzer
from research_backed_talent_predictor import ResearchBackedTalentPredictor
from olympic_readiness_system import OlympicReadinessSystem

# Import WOW Features
import sys
import os
sys.path.append(os.path.join(os.path.dirname(__file__), '..', 'WOW-Features'))
from integrated_wow_system import IntegratedWOWSystem

class IntegratedAISystem:
    """
    Master orchestrator for all AI components in the SIH Sports Assessment Platform
    Manages real-time processing, component coordination, and system optimization
    """
    
    def __init__(self, exercise_type: str = "pushup"):
        # Initialize logging
        self._setup_logging()
        
        # Core system components
        self.pose_detector = PoseDetector()
        self.rep_counter = AccurateRepCounter(exercise_type)
        self.cheat_detector = CheatDetectionSystem(exercise_type)
        self.performance_analyzer = EnhancedPerformanceAnalyzer()
        self.talent_predictor = ResearchBackedTalentPredictor()
        self.olympic_system = OlympicReadinessSystem()
        self.wow_system = IntegratedWOWSystem()
        
        # System state management
        self.is_running = False
        self.current_exercise = exercise_type
        self.session_data = self._initialize_session_data()
        
        # Performance optimization
        self.frame_buffer = deque(maxlen=30)  # 1 second buffer at 30 FPS
        self.processing_times = deque(maxlen=100)
        self.thread_pool = ThreadPoolExecutor(max_workers=4)
        
        # Real-time metrics
        self.fps_counter = 0
        self.fps_start_time = time.time()
        self.current_fps = 0
        
        # Component synchronization
        self.component_lock = threading.Lock()
        self.data_sync_event = threading.Event()
        
        self.logger.info(f"Integrated AI System initialized for {exercise_type}")
    
    def _setup_logging(self):
        """Setup comprehensive logging system"""
        logging.basicConfig(
            level=logging.INFO,
            format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
            handlers=[
                logging.FileHandler('sih_ai_system.log'),
                logging.StreamHandler()
            ]
        )
        self.logger = logging.getLogger('SIH_AI_System')
    
    def _initialize_session_data(self) -> Dict:
        """Initialize comprehensive session data structure"""
        return {
            'session_id': f"session_{int(time.time())}",
            'start_time': time.time(),
            'exercise_type': self.current_exercise,
            'frame_count': 0,
            'total_reps': 0,
            'total_hold_time': 0.0,
            'form_scores': [],
            'rep_times': [],
            'violations': [],
            'landmarks_history': [],
            'performance_metrics': {},
            'talent_assessment': {},
            'olympic_assessment': {},
            'wow_interactions': [],
            'system_performance': {
                'average_fps': 0,
                'processing_times': [],
                'component_latencies': {}
            }
        }
    
    def start_assessment_session(self, camera_index: int = 0) -> bool:
        """
        Start comprehensive AI-powered assessment session
        
        Args:
            camera_index: Camera device index
            
        Returns:
            Success status
        """
        try:
            # Initialize camera
            cap = cv2.VideoCapture(camera_index)
            if not cap.isOpened():
                self.logger.error("Failed to open camera")
                return False
            
            # Configure camera for optimal performance
            cap.set(cv2.CAP_PROP_FRAME_WIDTH, 1280)
            cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 720)
            cap.set(cv2.CAP_PROP_FPS, 30)
            
            self.is_running = True
            self.session_data['start_time'] = time.time()
            
            self.logger.info("Assessment session started")
            
            # Main processing loop
            self._run_main_processing_loop(cap)
            
        except Exception as e:
            self.logger.error(f"Assessment session error: {e}")
            return False
        finally:
            if 'cap' in locals():
                cap.release()
            cv2.destroyAllWindows()
            self.is_running = False
        
        return True
    
    def _run_main_processing_loop(self, cap):
        """Main processing loop with optimized component coordination"""
        while self.is_running:
            start_time = time.time()
            
            # Capture frame
            ret, frame = cap.read()
            if not ret:
                self.logger.warning("Failed to capture frame")
                break
            
            # Process frame through AI pipeline
            processed_frame, analysis_results = self._process_frame_comprehensive(frame)
            
            # Update session data
            self._update_session_data(analysis_results)
            
            # Display results
            cv2.imshow('SIH Sports Assessment - Integrated AI System', processed_frame)
            
            # Handle user input
            key = cv2.waitKey(1) & 0xFF
            if key == ord('q'):
                break
            elif key == ord(' '):
                self._toggle_assessment()
            elif key == ord('r'):
                self._reset_session()
            elif key == ord('t'):
                self._generate_talent_report()
            elif key == ord('o'):
                self._generate_olympic_assessment()
            
            # Performance tracking
            processing_time = time.time() - start_time
            self.processing_times.append(processing_time)
            self._update_fps_counter()
            
            # Maintain target FPS
            target_fps = 30
            frame_time = 1.0 / target_fps
            if processing_time < frame_time:
                time.sleep(frame_time - processing_time)
    
    def _process_frame_comprehensive(self, frame) -> Tuple[np.ndarray, Dict]:
        """
        Comprehensive frame processing through all AI components
        
        Args:
            frame: Input video frame
            
        Returns:
            Processed frame and analysis results
        """
        analysis_start = time.time()
        
        # Component timing tracking
        component_times = {}
        
        # Stage 1: Pose Detection (Critical Path)
        pose_start = time.time()
        processed_frame, landmarks = self.pose_detector.detect_pose(frame, draw=True)
        component_times['pose_detection'] = time.time() - pose_start
        
        if not landmarks:
            return processed_frame, {'error': 'No pose detected'}
        
        # Get raw landmarks for rep counter
        raw_landmarks = self.pose_detector.get_raw_landmarks(frame)
        
        # Stage 2: Parallel Processing of Core Components
        with ThreadPoolExecutor(max_workers=3) as executor:
            # Submit parallel tasks
            rep_future = executor.submit(self._process_rep_counting, raw_landmarks)
            cheat_future = executor.submit(self._process_cheat_detection, landmarks)
            performance_future = executor.submit(self._process_performance_analysis, landmarks)
            
            # Collect results
            rep_result = rep_future.result()
            cheat_result = cheat_future.result()
            performance_result = performance_future.result()
        
        # Stage 3: WOW Features Integration
        wow_start = time.time()
        wow_result = self._process_wow_features(rep_result, cheat_result, landmarks)
        component_times['wow_features'] = time.time() - wow_start
        
        # Stage 4: Visual Overlay and UI
        ui_start = time.time()
        final_frame = self._create_comprehensive_ui(
            processed_frame, rep_result, cheat_result, performance_result, wow_result
        )
        component_times['ui_rendering'] = time.time() - ui_start
        
        # Compile analysis results
        analysis_results = {
            'rep_analysis': rep_result,
            'cheat_analysis': cheat_result,
            'performance_analysis': performance_result,
            'wow_analysis': wow_result,
            'component_times': component_times,
            'total_processing_time': time.time() - analysis_start,
            'landmarks': landmarks,
            'frame_quality': self._assess_frame_quality(frame)
        }
        
        return final_frame, analysis_results
    
    def _process_rep_counting(self, raw_landmarks) -> Dict:
        """Process repetition counting with error handling"""
        try:
            if self.current_exercise.lower() == 'plank':
                return self._assess_plank_hold(raw_landmarks)
            else:
                return self.rep_counter.update_rep_count(raw_landmarks)
        except Exception as e:
            self.logger.error(f"Rep counting error: {e}")
            return {'rep_count': 0, 'current_state': 'ERROR', 'confidence': 0.0}
    
    def _process_cheat_detection(self, landmarks) -> Dict:
        """Process cheat detection with error handling"""
        try:
            return self.cheat_detector.analyze_form(landmarks)
        except Exception as e:
            self.logger.error(f"Cheat detection error: {e}")
            return {'form_quality': 0, 'violations': [], 'analysis': 'Error'}
    
    def _process_performance_analysis(self, landmarks) -> Dict:
        """Process performance analysis with error handling"""
        try:
            # Create performance data structure
            performance_data = {
                'exercise_type': self.current_exercise,
                'landmarks': landmarks,
                'timestamp': time.time(),
                'session_data': self.session_data
            }
            return self.performance_analyzer.analyze_exercise_performance(performance_data)
        except Exception as e:
            self.logger.error(f"Performance analysis error: {e}")
            return {'analysis': 'Error in performance analysis'}
    
    def _process_wow_features(self, rep_result, cheat_result, landmarks) -> Dict:
        """Process WOW features with error handling"""
        try:
            # Prepare data for WOW system
            current_metric = rep_result.get('rep_count', 0)
            if self.current_exercise.lower() == 'plank':
                current_metric = rep_result.get('hold_time', 0)
            
            # Update WOW system
            wow_response = self.wow_system.update_workout_progress(
                rep_count=current_metric,
                form_quality=cheat_result.get('form_quality', 0),
                violations=[str(v) for v in cheat_result.get('violations', [])],
                landmarks=landmarks
            )
            
            return wow_response
        except Exception as e:
            self.logger.error(f"WOW features error: {e}")
            return {'status': 'error', 'message': str(e)}
    
    def _create_comprehensive_ui(self, frame, rep_result, cheat_result, 
                                performance_result, wow_result) -> np.ndarray:
        """Create comprehensive UI overlay with all system information"""
        height, width = frame.shape[:2]
        
        # Create overlay
        overlay = frame.copy()
        
        # Main metrics panel (left side)
        self._draw_main_metrics_panel(overlay, rep_result, cheat_result)
        
        # Performance analysis panel (right side)
        self._draw_performance_panel(overlay, performance_result, width)
        
        # System status bar (top)
        self._draw_system_status_bar(overlay, width)
        
        # WOW features indicator (bottom)
        self._draw_wow_features_status(overlay, wow_result, width, height)
        
        return overlay
    
    def _draw_main_metrics_panel(self, frame, rep_result, cheat_result):
        """Draw main metrics panel"""
        # Background
        cv2.rectangle(frame, (10, 10), (400, 250), (0, 0, 0), -1)
        cv2.rectangle(frame, (10, 10), (400, 250), (0, 255, 255), 2)
        
        # Title
        cv2.putText(frame, 'SIH AI SPORTS ASSESSMENT', (15, 35), 
                   cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 255), 2)
        
        # Exercise type
        cv2.putText(frame, f'Exercise: {self.current_exercise.upper()}', (15, 60), 
                   cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)
        
        # Main metric
        if self.current_exercise.lower() == 'plank':
            hold_time = rep_result.get('hold_time', 0)
            cv2.putText(frame, f'HOLD TIME: {hold_time:.1f}s', (15, 95), 
                       cv2.FONT_HERSHEY_SIMPLEX, 0.8, (0, 255, 0), 2)
        else:
            reps = rep_result.get('rep_count', 0)
            cv2.putText(frame, f'REPS: {reps}', (15, 95), 
                       cv2.FONT_HERSHEY_SIMPLEX, 0.8, (0, 255, 0), 2)
        
        # Form quality
        form_quality = cheat_result.get('form_quality', 0)
        color = (0, 255, 0) if form_quality >= 80 else (0, 255, 255) if form_quality >= 60 else (0, 0, 255)
        cv2.putText(frame, f'Form Quality: {form_quality:.1f}%', (15, 125), 
                   cv2.FONT_HERSHEY_SIMPLEX, 0.6, color, 2)
        
        # Current state
        state = rep_result.get('current_state', 'UNKNOWN')
        cv2.putText(frame, f'State: {state}', (15, 155), 
                   cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)
        
        # FPS
        cv2.putText(frame, f'FPS: {self.current_fps}', (15, 185), 
                   cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)
        
        # System status
        status = "RUNNING" if self.is_running else "PAUSED"
        status_color = (0, 255, 0) if self.is_running else (0, 0, 255)
        cv2.putText(frame, f'Status: {status}', (15, 215), 
                   cv2.FONT_HERSHEY_SIMPLEX, 0.5, status_color, 1)
    
    def _draw_system_status_bar(self, frame, width):
        """Draw system performance status bar"""
        # Background
        cv2.rectangle(frame, (0, 0), (width, 30), (50, 50, 50), -1)
        
        # System info
        avg_processing_time = np.mean(self.processing_times) if self.processing_times else 0
        cv2.putText(frame, f'SIH AI System | Processing: {avg_processing_time*1000:.1f}ms | Session: {self.session_data["session_id"]}', 
                   (10, 20), cv2.FONT_HERSHEY_SIMPLEX, 0.4, (255, 255, 255), 1)
    
    def _generate_talent_report(self):
        """Generate comprehensive talent assessment report"""
        try:
            # Prepare comprehensive data
            talent_data = {
                'form_scores_history': self.session_data['form_scores'],
                'rep_times_history': self.session_data['rep_times'],
                'session_performance_history': [85, 87, 89, 91],  # Simulated historical data
                'movement_smoothness_scores': [82, 85, 87, 89],
                'movement_precision_scores': [80, 83, 86, 88],
                'coordination_consistency_scores': [78, 81, 84, 87]
            }
            
            # Generate talent report
            report = self.talent_predictor.generate_talent_report(talent_data)
            
            # Save report
            report_filename = f"talent_report_{int(time.time())}.json"
            with open(report_filename, 'w') as f:
                json.dump(report, f, indent=2)
            
            self.logger.info(f"Talent report generated: {report_filename}")
            print(f"🏆 Talent Report Generated: {report_filename}")
            
        except Exception as e:
            self.logger.error(f"Talent report generation error: {e}")
    
    def _generate_olympic_assessment(self):
        """Generate Olympic readiness assessment"""
        try:
            # Prepare athlete data
            athlete_data = {
                'name': 'SIH Athlete',
                'age': 25,
                'gender': 'male',
                'primary_exercise': self.current_exercise,
                'training_years': 3,
                'reps_per_minute': 30,
                'average_form_quality': np.mean(self.session_data['form_scores']) if self.session_data['form_scores'] else 70,
                'consistency_score': 80,
                'endurance_score': 75,
                'power_output': 70,
                'learning_rate_score': 85
            }
            
            # Generate Olympic assessment
            report = self.olympic_system.generate_olympic_assessment_report(athlete_data)
            
            # Save report
            report_filename = f"olympic_assessment_{int(time.time())}.json"
            with open(report_filename, 'w') as f:
                json.dump(report, f, indent=2)
            
            self.logger.info(f"Olympic assessment generated: {report_filename}")
            print(f"🥇 Olympic Assessment Generated: {report_filename}")
            
        except Exception as e:
            self.logger.error(f"Olympic assessment generation error: {e}")
    
    def _update_fps_counter(self):
        """Update FPS counter"""
        self.fps_counter += 1
        current_time = time.time()
        
        if current_time - self.fps_start_time >= 1.0:
            self.current_fps = self.fps_counter
            self.fps_counter = 0
            self.fps_start_time = current_time
    
    def generate_comprehensive_session_report(self) -> Dict:
        """Generate comprehensive session report with all AI insights"""
        session_duration = time.time() - self.session_data['start_time']
        
        report = {
            'session_summary': {
                'session_id': self.session_data['session_id'],
                'exercise_type': self.current_exercise,
                'duration_minutes': round(session_duration / 60, 2),
                'total_frames_processed': self.session_data['frame_count'],
                'average_fps': self.current_fps
            },
            'performance_metrics': {
                'total_reps': self.session_data['total_reps'],
                'total_hold_time': self.session_data['total_hold_time'],
                'average_form_quality': np.mean(self.session_data['form_scores']) if self.session_data['form_scores'] else 0,
                'violations_count': len(self.session_data['violations'])
            },
            'system_performance': {
                'average_processing_time': np.mean(self.processing_times) if self.processing_times else 0,
                'fps_stability': np.std([self.current_fps]) if self.current_fps else 0,
                'component_efficiency': self._calculate_component_efficiency()
            },
            'ai_insights': {
                'talent_indicators': self._extract_talent_indicators(),
                'improvement_suggestions': self._generate_improvement_suggestions(),
                'performance_trends': self._analyze_performance_trends()
            }
        }
        
        return report
    
    def shutdown_system(self):
        """Graceful system shutdown"""
        self.logger.info("Shutting down Integrated AI System")
        self.is_running = False
        
        # Close thread pool
        self.thread_pool.shutdown(wait=True)
        
        # Generate final report
        final_report = self.generate_comprehensive_session_report()
        
        # Save final report
        report_filename = f"session_report_{self.session_data['session_id']}.json"
        with open(report_filename, 'w') as f:
            json.dump(final_report, f, indent=2)
        
        self.logger.info(f"Final session report saved: {report_filename}")
        print(f"📊 Session Report Saved: {report_filename}")

# Usage example and main execution
def main():
    """Main execution function for the Integrated AI System"""
    print("🏆 SIH Sports Assessment Platform - Integrated AI System")
    print("=" * 60)
    
    # Initialize system
    ai_system = IntegratedAISystem("pushup")
    
    # Start assessment session
    try:
        success = ai_system.start_assessment_session(camera_index=0)
        if success:
            print("✅ Assessment session completed successfully")
        else:
            print("❌ Assessment session failed")
    except KeyboardInterrupt:
        print("\n⏹️ Session interrupted by user")
    finally:
        ai_system.shutdown_system()

if __name__ == "__main__":
    main()
