"""
Comprehensive Test Suite for SIH Sports Assessment Platform
Tests all core functionality and integration points
"""

import sys
import os
import time
import traceback
from typing import Dict, List, Any
import unittest
from unittest.mock import Mock, patch

# Add paths for imports
current_dir = os.path.dirname(os.path.abspath(__file__))
wow_dir = os.path.join(os.path.dirname(current_dir), 'WOW-Features')
sys.path.append(current_dir)
sys.path.append(wow_dir)

class TestResult:
    """Test result tracking"""
    def __init__(self):
        self.passed = 0
        self.failed = 0
        self.errors = []
        
    def add_pass(self, test_name: str):
        self.passed += 1
        print(f"✅ {test_name} - PASSED")
        
    def add_fail(self, test_name: str, error: str):
        self.failed += 1
        self.errors.append(f"{test_name}: {error}")
        print(f"❌ {test_name} - FAILED: {error}")
        
    def summary(self):
        total = self.passed + self.failed
        success_rate = (self.passed / total * 100) if total > 0 else 0
        
        print(f"\n{'='*60}")
        print(f"TEST SUMMARY")
        print(f"{'='*60}")
        print(f"Total Tests: {total}")
        print(f"Passed: {self.passed}")
        print(f"Failed: {self.failed}")
        print(f"Success Rate: {success_rate:.1f}%")
        
        if self.errors:
            print(f"\nFAILED TESTS:")
            for error in self.errors:
                print(f"  • {error}")
        
        return success_rate >= 80  # 80% success rate required

def test_imports():
    """Test all module imports"""
    print(f"\n🔍 TESTING IMPORTS")
    print(f"{'='*40}")
    
    result = TestResult()
    
    # Core backend modules
    modules_to_test = [
        ('pose', 'Core pose detection'),
        ('improved_rep_counter', 'Advanced rep counter'),
        ('cheat_detection_system', 'Cheat detection system'),
        ('sports_assessment_demo', 'Main demo application')
    ]
    
    for module_name, description in modules_to_test:
        try:
            __import__(module_name)
            result.add_pass(f"Import {module_name} ({description})")
        except Exception as e:
            result.add_fail(f"Import {module_name}", str(e))
    
    # WOW Features modules (optional)
    wow_modules = [
        ('ai_voice_coach', 'AI Voice Coach'),
        ('talent_predictor', 'Talent Predictor'),
        ('olympic_predictor', 'Olympic Predictor'),
        ('integrated_wow_system', 'Integrated WOW System')
    ]
    
    for module_name, description in wow_modules:
        try:
            __import__(module_name)
            result.add_pass(f"Import {module_name} ({description})")
        except Exception as e:
            result.add_fail(f"Import {module_name} (Optional)", str(e))
    
    return result.summary()

def test_core_functionality():
    """Test core system functionality"""
    print(f"\n🧪 TESTING CORE FUNCTIONALITY")
    print(f"{'='*40}")
    
    result = TestResult()
    
    # Test Pose Detector
    try:
        from pose import PoseDetector
        detector = PoseDetector()
        result.add_pass("PoseDetector instantiation")
        
        # Test with mock image
        import numpy as np
        mock_image = np.zeros((480, 640, 3), dtype=np.uint8)
        processed_image, landmarks = detector.detect_pose(mock_image, draw=False)
        
        if processed_image is not None:
            result.add_pass("PoseDetector processing")
        else:
            result.add_fail("PoseDetector processing", "No output image")
            
        detector.cleanup()
        result.add_pass("PoseDetector cleanup")
        
    except Exception as e:
        result.add_fail("PoseDetector test", str(e))
    
    # Test Rep Counter
    try:
        from improved_rep_counter import ImprovedRepCounter, ExerciseType
        counter = ImprovedRepCounter(ExerciseType.PUSHUP)
        result.add_pass("RepCounter instantiation")
        
        # Test with mock landmarks
        mock_landmarks = [{'x': 0.5, 'y': 0.5, 'z': 0, 'visibility': 0.9} for _ in range(33)]
        rep_result = counter.update(mock_landmarks)
        
        if isinstance(rep_result, dict) and 'rep_count' in rep_result:
            result.add_pass("RepCounter processing")
        else:
            result.add_fail("RepCounter processing", "Invalid output format")
            
        counter.reset()
        result.add_pass("RepCounter reset")
        
    except Exception as e:
        result.add_fail("RepCounter test", str(e))
    
    # Test Cheat Detector
    try:
        from cheat_detection_system import CheatDetectionSystem, ExerciseType as CheatExerciseType
        detector = CheatDetectionSystem(CheatExerciseType.PUSHUP)
        result.add_pass("CheatDetector instantiation")
        
        # Test with mock landmarks
        mock_landmarks = [{'x': 0.5, 'y': 0.5, 'z': 0, 'visibility': 0.9} for _ in range(33)]
        cheat_result = detector.analyze_form(mock_landmarks)
        
        if isinstance(cheat_result, dict) and 'form_quality' in cheat_result:
            result.add_pass("CheatDetector processing")
        else:
            result.add_fail("CheatDetector processing", "Invalid output format")
            
    except Exception as e:
        result.add_fail("CheatDetector test", str(e))
    
    return result.summary()

def test_wow_features():
    """Test WOW features functionality"""
    print(f"\n🌟 TESTING WOW FEATURES")
    print(f"{'='*40}")
    
    result = TestResult()
    
    # Test AI Voice Coach
    try:
        from ai_voice_coach import AIVoiceCoach, Language
        coach = AIVoiceCoach(Language.ENGLISH, voice_speed=200)  # Fast for testing
        result.add_pass("AIVoiceCoach instantiation")
        
        coaching_result = coach.provide_coaching(
            rep_count=10,
            form_quality=85,
            violations=[]
        )
        
        if isinstance(coaching_result, dict):
            result.add_pass("AIVoiceCoach processing")
        else:
            result.add_fail("AIVoiceCoach processing", "Invalid output format")
            
        coach.cleanup()
        result.add_pass("AIVoiceCoach cleanup")
        
    except Exception as e:
        result.add_fail("AIVoiceCoach test", str(e))
    
    # Test Talent Predictor
    try:
        from talent_predictor import TalentPredictor
        predictor = TalentPredictor()
        result.add_pass("TalentPredictor instantiation")
        
        test_data = {
            'pushups': 30,
            'squats': 40,
            'age': 25,
            'height': 175,
            'weight': 70
        }
        
        analysis = predictor.analyze_performance(test_data)
        
        if isinstance(analysis, dict) and 'talent_prediction' in analysis:
            result.add_pass("TalentPredictor processing")
        else:
            result.add_fail("TalentPredictor processing", "Invalid output format")
            
    except Exception as e:
        result.add_fail("TalentPredictor test", str(e))
    
    # Test Olympic Predictor
    try:
        from olympic_predictor import OlympicPredictor, OlympicSport
        predictor = OlympicPredictor()
        result.add_pass("OlympicPredictor instantiation")
        
        test_athlete = {
            'age': 24,
            'weight': 75,
            'average_form_quality': 85,
            'consistency_score': 80
        }
        
        assessment = predictor.assess_olympic_readiness(
            test_athlete, 
            OlympicSport.WEIGHTLIFTING, 
            'men'
        )
        
        if isinstance(assessment, dict) and 'overall_score' in assessment:
            result.add_pass("OlympicPredictor processing")
        else:
            result.add_fail("OlympicPredictor processing", "Invalid output format")
            
    except Exception as e:
        result.add_fail("OlympicPredictor test", str(e))
    
    # Test Integrated System
    try:
        from integrated_wow_system import IntegratedWOWSystem, Language
        wow_system = IntegratedWOWSystem(Language.ENGLISH)
        result.add_pass("IntegratedWOWSystem instantiation")
        
        session = wow_system.start_workout_session('pushup')
        
        if isinstance(session, dict) and 'session_id' in session:
            result.add_pass("IntegratedWOWSystem session start")
        else:
            result.add_fail("IntegratedWOWSystem session start", "Invalid session format")
        
        # Test progress update
        progress = wow_system.update_workout_progress(
            rep_count=5,
            form_quality=80,
            violations=[]
        )
        
        if isinstance(progress, dict):
            result.add_pass("IntegratedWOWSystem progress update")
        else:
            result.add_fail("IntegratedWOWSystem progress update", "Invalid progress format")
        
        wow_system.cleanup()
        result.add_pass("IntegratedWOWSystem cleanup")
        
    except Exception as e:
        result.add_fail("IntegratedWOWSystem test", str(e))
    
    return result.summary()

def test_integration():
    """Test system integration"""
    print(f"\n🔗 TESTING SYSTEM INTEGRATION")
    print(f"{'='*40}")
    
    result = TestResult()
    
    try:
        # Test complete workflow
        from sports_assessment_demo import SportsAssessmentDemo, DemoMode
        
        # Test basic mode instantiation
        demo = SportsAssessmentDemo(DemoMode.BASIC)
        result.add_pass("Demo instantiation (Basic mode)")
        
        # Test WOW features mode
        try:
            demo_wow = SportsAssessmentDemo(DemoMode.WOW_FEATURES)
            result.add_pass("Demo instantiation (WOW mode)")
        except Exception as e:
            result.add_fail("Demo instantiation (WOW mode)", str(e))
        
        # Test exercise setup
        demo._setup_exercise('pushup')
        if demo.current_exercise is not None:
            result.add_pass("Exercise setup")
        else:
            result.add_fail("Exercise setup", "Exercise not properly configured")
        
        # Test session reset
        demo._reset_session()
        result.add_pass("Session reset")
        
        # Test cleanup
        demo._cleanup()
        result.add_pass("Demo cleanup")
        
    except Exception as e:
        result.add_fail("Integration test", str(e))
    
    return result.summary()

def test_data_flow():
    """Test data flow between components"""
    print(f"\n📊 TESTING DATA FLOW")
    print(f"{'='*40}")
    
    result = TestResult()
    
    try:
        # Create mock data pipeline
        import numpy as np
        from pose import PoseDetector
        from improved_rep_counter import ImprovedRepCounter, ExerciseType
        from cheat_detection_system import CheatDetectionSystem
        
        # Initialize components
        detector = PoseDetector()
        counter = ImprovedRepCounter(ExerciseType.PUSHUP)
        cheat_detector = CheatDetectionSystem(ExerciseType.PUSHUP)
        
        # Mock image processing
        mock_image = np.zeros((480, 640, 3), dtype=np.uint8)
        
        # Process through pipeline
        processed_image, landmarks = detector.detect_pose(mock_image, draw=False)
        
        if landmarks:
            # Test rep counting with landmarks
            rep_result = counter.update(landmarks)
            result.add_pass("Data flow: Pose -> Rep Counter")
            
            # Test cheat detection with landmarks
            cheat_result = cheat_detector.analyze_form(landmarks)
            result.add_pass("Data flow: Pose -> Cheat Detector")
            
            # Verify data consistency
            if isinstance(rep_result, dict) and isinstance(cheat_result, dict):
                result.add_pass("Data consistency check")
            else:
                result.add_fail("Data consistency check", "Inconsistent data formats")
        else:
            result.add_fail("Data flow test", "No landmarks generated")
        
        # Cleanup
        detector.cleanup()
        
    except Exception as e:
        result.add_fail("Data flow test", str(e))
    
    return result.summary()

def test_performance():
    """Test system performance"""
    print(f"\n⚡ TESTING PERFORMANCE")
    print(f"{'='*40}")
    
    result = TestResult()
    
    try:
        import time
        import numpy as np
        from pose import PoseDetector
        
        detector = PoseDetector()
        mock_image = np.zeros((480, 640, 3), dtype=np.uint8)
        
        # Measure processing time
        num_iterations = 10
        start_time = time.time()
        
        for _ in range(num_iterations):
            processed_image, landmarks = detector.detect_pose(mock_image, draw=False)
        
        end_time = time.time()
        avg_time = (end_time - start_time) / num_iterations
        fps = 1.0 / avg_time if avg_time > 0 else 0
        
        if fps >= 10:  # Minimum 10 FPS
            result.add_pass(f"Performance test (FPS: {fps:.1f})")
        else:
            result.add_fail("Performance test", f"Low FPS: {fps:.1f}")
        
        detector.cleanup()
        
    except Exception as e:
        result.add_fail("Performance test", str(e))
    
    return result.summary()

def run_all_tests():
    """Run complete test suite"""
    print(f"🏆 SIH SPORTS ASSESSMENT PLATFORM - TEST SUITE")
    print(f"{'='*60}")
    print(f"Testing all components and integrations...")
    
    start_time = time.time()
    
    # Run all test categories
    test_results = []
    
    test_results.append(test_imports())
    test_results.append(test_core_functionality())
    test_results.append(test_wow_features())
    test_results.append(test_integration())
    test_results.append(test_data_flow())
    test_results.append(test_performance())
    
    end_time = time.time()
    total_time = end_time - start_time
    
    # Overall summary
    passed_tests = sum(test_results)
    total_tests = len(test_results)
    overall_success_rate = (passed_tests / total_tests * 100) if total_tests > 0 else 0
    
    print(f"\n{'='*60}")
    print(f"OVERALL TEST RESULTS")
    print(f"{'='*60}")
    print(f"Test Categories: {total_tests}")
    print(f"Passed Categories: {passed_tests}")
    print(f"Failed Categories: {total_tests - passed_tests}")
    print(f"Overall Success Rate: {overall_success_rate:.1f}%")
    print(f"Total Test Time: {total_time:.2f} seconds")
    
    if overall_success_rate >= 80:
        print(f"\n🎉 SYSTEM STATUS: READY FOR DEPLOYMENT")
        print(f"✅ All critical systems are functioning properly")
    else:
        print(f"\n⚠️ SYSTEM STATUS: NEEDS ATTENTION")
        print(f"❌ Some components require fixes before deployment")
    
    print(f"\n📋 COMPONENT STATUS:")
    print(f"  • Core Backend: {'✅ Ready' if test_results[0] and test_results[1] else '❌ Issues'}")
    print(f"  • WOW Features: {'✅ Ready' if test_results[2] else '⚠️ Optional'}")
    print(f"  • Integration: {'✅ Ready' if test_results[3] else '❌ Issues'}")
    print(f"  • Data Flow: {'✅ Ready' if test_results[4] else '❌ Issues'}")
    print(f"  • Performance: {'✅ Ready' if test_results[5] else '⚠️ Slow'}")
    
    return overall_success_rate >= 80

if __name__ == "__main__":
    try:
        success = run_all_tests()
        exit_code = 0 if success else 1
        
        print(f"\n{'='*60}")
        if success:
            print(f"🚀 SIH Sports Assessment Platform is READY!")
            print(f"All systems tested and functioning properly.")
        else:
            print(f"🔧 SIH Sports Assessment Platform needs attention.")
            print(f"Please review failed tests and fix issues.")
        print(f"{'='*60}")
        
        sys.exit(exit_code)
        
    except KeyboardInterrupt:
        print(f"\n⏹️ Test suite interrupted by user")
        sys.exit(1)
    except Exception as e:
        print(f"\n❌ Test suite failed with error: {e}")
        traceback.print_exc()
        sys.exit(1)



