"""
Integrated WOW Features System
Main integration hub for all advanced AI features
"""

import sys
import os
import time
import json
from typing import Dict, List, Optional, Any
from enum import Enum
import threading
import queue
from dataclasses import dataclass

# Add current directory to path for imports
sys.path.append(os.path.dirname(os.path.abspath(__file__)))
sys.path.append(os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'Python-Backend'))

try:
    from ai_voice_coach import AIVoiceCoach, Language, CoachingType
    from talent_predictor import TalentPredictor, SportCategory, TalentLevel
    from olympic_predictor import OlympicPredictor, OlympicSport, ReadinessLevel
except ImportError as e:
    print(f"Import warning: {e}")

@dataclass
class WorkoutSession:
    """Data structure for workout session"""
    exercise_type: str
    start_time: float
    rep_count: int = 0
    form_quality_scores: List[float] = None
    violations: List[str] = None
    is_complete: bool = False
    
    def __post_init__(self):
        if self.form_quality_scores is None:
            self.form_quality_scores = []
        if self.violations is None:
            self.violations = []

class WOWFeatureType(Enum):
    AI_VOICE_COACH = "ai_voice_coach"
    TALENT_PREDICTION = "talent_prediction"
    OLYMPIC_ASSESSMENT = "olympic_assessment"
    AR_FORM_GUIDE = "ar_form_guide"
    MULTILANG_INTERFACE = "multilang_interface"

class IntegratedWOWSystem:
    """
    Integrated system managing all WOW features
    """
    
    def __init__(self, language: Language = Language.ENGLISH):
        self.language = language
        self.enabled_features = set()
        
        # Initialize core components
        self.voice_coach = None
        self.talent_predictor = None
        self.olympic_predictor = None
        
        # Session management
        self.current_session = None
        self.session_history = []
        
        # Feature coordination
        self.feature_queue = queue.Queue()
        self.coordination_thread = None
        self.is_running = False
        
        # Performance tracking
        self.performance_data = {
            'sessions_completed': 0,
            'total_reps': 0,
            'average_form_quality': 0,
            'improvement_trend': []
        }
        
        # Initialize system
        self._initialize_components()
    
    def _initialize_components(self):
        """Initialize all WOW feature components"""
        try:
            # Initialize AI Voice Coach
            self.voice_coach = AIVoiceCoach(self.language, voice_speed=160)
            self.enabled_features.add(WOWFeatureType.AI_VOICE_COACH)
            print("✅ AI Voice Coach initialized")
            
            # Initialize Talent Predictor
            self.talent_predictor = TalentPredictor()
            self.enabled_features.add(WOWFeatureType.TALENT_PREDICTION)
            print("✅ Talent Predictor initialized")
            
            # Initialize Olympic Predictor
            self.olympic_predictor = OlympicPredictor()
            self.enabled_features.add(WOWFeatureType.OLYMPIC_ASSESSMENT)
            print("✅ Olympic Predictor initialized")
            
            # Start coordination thread
            self._start_coordination_thread()
            
        except Exception as e:
            print(f"❌ Error initializing WOW components: {e}")
    
    def _start_coordination_thread(self):
        """Start background thread for feature coordination"""
        def coordination_worker():
            while self.is_running:
                try:
                    # Process feature coordination tasks
                    if not self.feature_queue.empty():
                        task = self.feature_queue.get(timeout=1)
                        self._process_coordination_task(task)
                    time.sleep(0.1)
                except queue.Empty:
                    continue
                except Exception as e:
                    print(f"Coordination thread error: {e}")
        
        self.is_running = True
        self.coordination_thread = threading.Thread(target=coordination_worker, daemon=True)
        self.coordination_thread.start()
    
    def start_workout_session(self, exercise_type: str, athlete_data: Optional[Dict] = None) -> Dict:
        """
        Start a new workout session with integrated WOW features
        
        Args:
            exercise_type: Type of exercise being performed
            athlete_data: Optional athlete biographical data
            
        Returns:
            Session initialization result
        """
        # End previous session if active
        if self.current_session and not self.current_session.is_complete:
            self.end_workout_session()
        
        # Create new session
        self.current_session = WorkoutSession(
            exercise_type=exercise_type,
            start_time=time.time()
        )
        
        # Reset voice coach for new session
        if self.voice_coach:
            self.voice_coach.reset_session()
        
        # Provide initial coaching
        initial_coaching = self._provide_initial_coaching(exercise_type)
        
        return {
            'session_id': id(self.current_session),
            'exercise_type': exercise_type,
            'features_enabled': [f.value for f in self.enabled_features],
            'initial_coaching': initial_coaching,
            'status': 'active'
        }
    
    def update_workout_progress(self, 
                              rep_count: int,
                              form_quality: float,
                              violations: List[str],
                              landmarks: Optional[List] = None) -> Dict:
        """
        Update workout progress and coordinate all WOW features
        
        Args:
            rep_count: Current repetition count
            form_quality: Current form quality score (0-100)
            violations: List of current form violations
            landmarks: Optional pose landmarks for AR features
            
        Returns:
            Coordinated feature responses
        """
        if not self.current_session:
            return {'error': 'No active session'}
        
        # Update session data
        self.current_session.rep_count = rep_count
        self.current_session.form_quality_scores.append(form_quality)
        self.current_session.violations.extend(violations)
        
        responses = {}
        
        # AI Voice Coach Response
        if WOWFeatureType.AI_VOICE_COACH in self.enabled_features and self.voice_coach:
            coaching_response = self.voice_coach.provide_coaching(
                rep_count=rep_count,
                form_quality=form_quality,
                violations=violations
            )
            responses['voice_coaching'] = coaching_response
        
        # AR Form Guide (simulated)
        if landmarks:
            ar_response = self._provide_ar_guidance(landmarks, form_quality, violations)
            responses['ar_guidance'] = ar_response
        
        # Real-time talent insights
        if rep_count > 0 and rep_count % 10 == 0:  # Every 10 reps
            talent_insights = self._provide_talent_insights()
            responses['talent_insights'] = talent_insights
        
        # Coordinate features
        self._coordinate_features(rep_count, form_quality, violations)
        
        return responses
    
    def end_workout_session(self, athlete_data: Optional[Dict] = None) -> Dict:
        """
        End current workout session and provide comprehensive analysis
        
        Args:
            athlete_data: Optional athlete data for detailed analysis
            
        Returns:
            Complete session analysis with all WOW features
        """
        if not self.current_session:
            return {'error': 'No active session'}
        
        # Mark session as complete
        self.current_session.is_complete = True
        session_duration = time.time() - self.current_session.start_time
        
        # Calculate session metrics
        avg_form_quality = (sum(self.current_session.form_quality_scores) / 
                           len(self.current_session.form_quality_scores) 
                           if self.current_session.form_quality_scores else 0)
        
        # Voice coach completion
        completion_coaching = None
        if self.voice_coach:
            completion_response = self.voice_coach.provide_coaching(
                rep_count=self.current_session.rep_count,
                form_quality=avg_form_quality,
                violations=[],
                exercise_complete=True
            )
            completion_coaching = completion_response
        
        # Talent analysis
        talent_analysis = None
        if self.talent_predictor and athlete_data:
            performance_data = self._compile_performance_data(athlete_data)
            talent_analysis = self.talent_predictor.analyze_performance(performance_data)
        
        # Olympic assessment
        olympic_assessment = None
        if (self.olympic_predictor and athlete_data and 
            talent_analysis and talent_analysis['talent_prediction']['level'] in ['elite', 'olympic_potential']):
            
            # Determine sport based on exercise
            sport = self._map_exercise_to_olympic_sport(self.current_session.exercise_type)
            if sport:
                olympic_assessment = self.olympic_predictor.assess_olympic_readiness(
                    athlete_data, sport, athlete_data.get('gender', 'men')
                )
        
        # Update performance tracking
        self._update_performance_tracking()
        
        # Add to session history
        self.session_history.append(self.current_session)
        
        # Comprehensive session summary
        session_summary = {
            'session_id': id(self.current_session),
            'exercise_type': self.current_session.exercise_type,
            'duration_minutes': session_duration / 60,
            'total_reps': self.current_session.rep_count,
            'average_form_quality': avg_form_quality,
            'total_violations': len(self.current_session.violations),
            'completion_coaching': completion_coaching,
            'talent_analysis': talent_analysis,
            'olympic_assessment': olympic_assessment,
            'performance_insights': self._generate_performance_insights(),
            'recommendations': self._generate_integrated_recommendations(talent_analysis, olympic_assessment)
        }
        
        # Clear current session
        self.current_session = None
        
        return session_summary
    
    def _provide_initial_coaching(self, exercise_type: str) -> Dict:
        """Provide initial coaching for exercise start"""
        coaching_messages = {
            'english': {
                'pushup': "Let's start with push-ups! Focus on proper form and controlled movement.",
                'squat': "Time for squats! Keep your back straight and go deep.",
                'situp': "Sit-ups time! Engage your core and control the movement.",
                'plank': "Plank position! Keep your body straight and core tight.",
                'burpee': "Burpees! Full body workout - let's do this!",
                'vertical_jump': "Vertical jumps! Explosive power - jump high!"
            },
            'hindi': {
                'pushup': "Push-ups shuru karte hain! Form pe dhyan do aur control se karo.",
                'squat': "Squats ka time! Back straight rakho aur deep jao.",
                'situp': "Sit-ups! Core tight rakho aur control se karo.",
                'plank': "Plank position! Body straight rakho aur core tight.",
                'burpee': "Burpees! Full body workout - chaliye karte hain!",
                'vertical_jump': "Vertical jumps! Explosive power - ooncha jump karo!"
            }
        }
        
        lang_key = 'hindi' if self.language == Language.HINDI else 'english'
        message = coaching_messages[lang_key].get(exercise_type, "Let's begin the workout!")
        
        if self.voice_coach:
            self.voice_coach._speak_async(message)
        
        return {
            'message': message,
            'type': 'initial_coaching',
            'language': self.language.value
        }
    
    def _provide_ar_guidance(self, landmarks: List, form_quality: float, violations: List[str]) -> Dict:
        """Provide AR form guidance (simulated)"""
        ar_elements = []
        
        # Form quality indicator
        if form_quality < 70:
            ar_elements.append({
                'type': 'form_warning',
                'message': 'Improve form for better results',
                'color': 'red',
                'position': 'center'
            })
        elif form_quality > 90:
            ar_elements.append({
                'type': 'form_excellent',
                'message': 'Excellent form!',
                'color': 'green',
                'position': 'center'
            })
        
        # Violation-specific guidance
        for violation in violations:
            if 'partial_range_of_motion' in violation:
                ar_elements.append({
                    'type': 'range_guide',
                    'message': 'Go deeper - full range of motion',
                    'color': 'orange',
                    'position': 'bottom'
                })
            elif 'body_alignment' in violation:
                ar_elements.append({
                    'type': 'alignment_guide',
                    'message': 'Keep body aligned',
                    'color': 'yellow',
                    'position': 'side'
                })
        
        return {
            'ar_elements': ar_elements,
            'form_quality': form_quality,
            'guidance_active': len(ar_elements) > 0
        }
    
    def _provide_talent_insights(self) -> Dict:
        """Provide real-time talent insights"""
        if not self.current_session:
            return {}
        
        avg_form = (sum(self.current_session.form_quality_scores) / 
                   len(self.current_session.form_quality_scores) 
                   if self.current_session.form_quality_scores else 0)
        
        insights = []
        
        # Performance insights
        if avg_form > 85:
            insights.append("Excellent form consistency - shows strong technical foundation")
        elif avg_form < 70:
            insights.append("Focus on form improvement for better talent development")
        
        # Rep count insights
        if self.current_session.rep_count >= 30:
            insights.append("Strong endurance showing - good for distance sports")
        elif self.current_session.rep_count >= 20:
            insights.append("Good strength endurance - suitable for power sports")
        
        return {
            'insights': insights,
            'current_performance_level': self._assess_current_level(avg_form, self.current_session.rep_count),
            'improvement_areas': self._identify_improvement_areas()
        }
    
    def _coordinate_features(self, rep_count: int, form_quality: float, violations: List[str]):
        """Coordinate between different WOW features"""
        # Add coordination task to queue
        coordination_task = {
            'type': 'performance_update',
            'data': {
                'rep_count': rep_count,
                'form_quality': form_quality,
                'violations': violations,
                'timestamp': time.time()
            }
        }
        
        if not self.feature_queue.full():
            self.feature_queue.put(coordination_task)
    
    def _process_coordination_task(self, task: Dict):
        """Process feature coordination tasks"""
        if task['type'] == 'performance_update':
            data = task['data']
            
            # Coordinate voice coaching timing
            if self.voice_coach and data['form_quality'] < 60:
                # Increase coaching frequency for poor form
                self.voice_coach.set_coaching_interval(2.0)
            elif self.voice_coach and data['form_quality'] > 85:
                # Reduce coaching frequency for good form
                self.voice_coach.set_coaching_interval(5.0)
    
    def _compile_performance_data(self, athlete_data: Dict) -> Dict:
        """Compile performance data for talent analysis"""
        if not self.current_session:
            return athlete_data
        
        # Add session performance data
        performance_data = athlete_data.copy()
        
        exercise_type = self.current_session.exercise_type
        rep_count = self.current_session.rep_count
        avg_form = (sum(self.current_session.form_quality_scores) / 
                   len(self.current_session.form_quality_scores) 
                   if self.current_session.form_quality_scores else 0)
        
        # Map exercise to performance metrics
        if exercise_type == 'pushup':
            performance_data['pushups'] = rep_count
        elif exercise_type == 'squat':
            performance_data['squats'] = rep_count
        elif exercise_type == 'situp':
            performance_data['situps'] = rep_count
        elif exercise_type == 'plank':
            performance_data['plank_time'] = time.time() - self.current_session.start_time
        
        performance_data['average_form_quality'] = avg_form
        performance_data['consistency_score'] = self._calculate_consistency_score()
        
        return performance_data
    
    def _map_exercise_to_olympic_sport(self, exercise_type: str) -> Optional[OlympicSport]:
        """Map exercise type to Olympic sport"""
        mapping = {
            'pushup': OlympicSport.WEIGHTLIFTING,
            'squat': OlympicSport.WEIGHTLIFTING,
            'situp': OlympicSport.ATHLETICS_DISTANCE,
            'vertical_jump': OlympicSport.ATHLETICS_SPRINT,
            'burpee': OlympicSport.BOXING
        }
        return mapping.get(exercise_type)
    
    def _calculate_consistency_score(self) -> float:
        """Calculate form consistency score"""
        if not self.current_session.form_quality_scores or len(self.current_session.form_quality_scores) < 3:
            return 75.0
        
        scores = self.current_session.form_quality_scores
        variance = sum((x - sum(scores)/len(scores))**2 for x in scores) / len(scores)
        consistency = max(0, 100 - variance)
        
        return consistency
    
    def _update_performance_tracking(self):
        """Update overall performance tracking"""
        if not self.current_session:
            return
        
        self.performance_data['sessions_completed'] += 1
        self.performance_data['total_reps'] += self.current_session.rep_count
        
        # Update average form quality
        if self.current_session.form_quality_scores:
            session_avg = sum(self.current_session.form_quality_scores) / len(self.current_session.form_quality_scores)
            current_avg = self.performance_data['average_form_quality']
            sessions = self.performance_data['sessions_completed']
            
            self.performance_data['average_form_quality'] = ((current_avg * (sessions - 1)) + session_avg) / sessions
        
        # Add to improvement trend
        if self.current_session.form_quality_scores:
            avg_form = sum(self.current_session.form_quality_scores) / len(self.current_session.form_quality_scores)
            self.performance_data['improvement_trend'].append({
                'session': self.performance_data['sessions_completed'],
                'form_quality': avg_form,
                'reps': self.current_session.rep_count,
                'timestamp': time.time()
            })
    
    def _generate_performance_insights(self) -> List[str]:
        """Generate performance insights from session data"""
        insights = []
        
        if not self.current_session:
            return insights
        
        # Form quality insights
        avg_form = (sum(self.current_session.form_quality_scores) / 
                   len(self.current_session.form_quality_scores) 
                   if self.current_session.form_quality_scores else 0)
        
        if avg_form > 90:
            insights.append("Exceptional form quality throughout the session")
        elif avg_form > 80:
            insights.append("Good form consistency with room for minor improvements")
        elif avg_form > 70:
            insights.append("Decent form but focus needed on technique refinement")
        else:
            insights.append("Form needs significant improvement for optimal results")
        
        # Rep count insights
        if self.current_session.rep_count >= 50:
            insights.append("Excellent endurance and strength demonstrated")
        elif self.current_session.rep_count >= 30:
            insights.append("Good strength endurance capacity shown")
        elif self.current_session.rep_count >= 15:
            insights.append("Moderate fitness level - consistent training will improve capacity")
        else:
            insights.append("Building foundational strength - keep up regular training")
        
        # Violation insights
        violation_count = len(self.current_session.violations)
        if violation_count == 0:
            insights.append("Perfect technique execution - no form violations detected")
        elif violation_count < 5:
            insights.append("Minor form issues detected - easily correctable with focus")
        else:
            insights.append("Multiple form violations - technique training recommended")
        
        return insights
    
    def _generate_integrated_recommendations(self, talent_analysis: Optional[Dict], olympic_assessment: Optional[Dict]) -> List[str]:
        """Generate integrated recommendations from all WOW features"""
        recommendations = []
        
        # Session-specific recommendations
        if self.current_session:
            avg_form = (sum(self.current_session.form_quality_scores) / 
                       len(self.current_session.form_quality_scores) 
                       if self.current_session.form_quality_scores else 0)
            
            if avg_form < 75:
                recommendations.append("Focus on form quality over quantity in next sessions")
            
            if self.current_session.rep_count < 20:
                recommendations.append("Build endurance gradually with consistent training")
        
        # Talent-based recommendations
        if talent_analysis:
            recommendations.extend(talent_analysis.get('recommendations', [])[:2])
        
        # Olympic-level recommendations
        if olympic_assessment:
            recommendations.extend(olympic_assessment.get('recommendations', [])[:2])
        
        # General WOW feature recommendations
        if self.language == Language.HINDI:
            recommendations.append("Continue using Hindi coaching for better understanding")
        
        recommendations.append("Utilize AR guidance for real-time form correction")
        recommendations.append("Track progress with AI talent insights")
        
        return recommendations[:6]  # Limit to top 6 recommendations
    
    def _assess_current_level(self, form_quality: float, rep_count: int) -> str:
        """Assess current performance level"""
        if form_quality >= 85 and rep_count >= 40:
            return "Advanced"
        elif form_quality >= 75 and rep_count >= 25:
            return "Intermediate"
        elif form_quality >= 65 and rep_count >= 15:
            return "Beginner Plus"
        else:
            return "Beginner"
    
    def _identify_improvement_areas(self) -> List[str]:
        """Identify key areas for improvement"""
        areas = []
        
        if not self.current_session:
            return areas
        
        avg_form = (sum(self.current_session.form_quality_scores) / 
                   len(self.current_session.form_quality_scores) 
                   if self.current_session.form_quality_scores else 0)
        
        if avg_form < 80:
            areas.append("Form Quality")
        
        if self.current_session.rep_count < 25:
            areas.append("Strength Endurance")
        
        if len(self.current_session.violations) > 5:
            areas.append("Technique Consistency")
        
        return areas
    
    def get_system_status(self) -> Dict:
        """Get comprehensive system status"""
        return {
            'enabled_features': [f.value for f in self.enabled_features],
            'active_session': self.current_session is not None,
            'language': self.language.value,
            'performance_summary': self.performance_data,
            'total_sessions': len(self.session_history),
            'system_health': 'optimal' if len(self.enabled_features) >= 3 else 'degraded'
        }
    
    def change_language(self, language: Language):
        """Change system language"""
        self.language = language
        if self.voice_coach:
            self.voice_coach.set_language(language)
    
    def enable_feature(self, feature: WOWFeatureType):
        """Enable specific WOW feature"""
        self.enabled_features.add(feature)
    
    def disable_feature(self, feature: WOWFeatureType):
        """Disable specific WOW feature"""
        self.enabled_features.discard(feature)
    
    def cleanup(self):
        """Cleanup system resources"""
        self.is_running = False
        
        if self.coordination_thread:
            self.coordination_thread.join(timeout=2)
        
        if self.voice_coach:
            self.voice_coach.cleanup()
        
        print("🔄 WOW System cleanup completed")

# Example usage and testing
if __name__ == "__main__":
    print("🚀 Testing Integrated WOW Features System...")
    print("="*60)
    
    # Initialize WOW system
    wow_system = IntegratedWOWSystem(Language.ENGLISH)
    
    # Test athlete data
    test_athlete = {
        'age': 24,
        'height': 175,
        'weight': 70,
        'gender': 'men',
        'training_years': 3,
        'competitions_participated': 8
    }
    
    # Test workout session
    print("\n🏋️ Starting workout session...")
    session_start = wow_system.start_workout_session('pushup', test_athlete)
    print(f"Session started: {session_start['session_id']}")
    print(f"Features enabled: {session_start['features_enabled']}")
    
    # Simulate workout progress
    print("\n📊 Simulating workout progress...")
    
    for rep in range(1, 26):
        # Simulate varying form quality and violations
        form_quality = 85 + (rep % 5) * 2 - 5  # Varies between 75-95
        violations = ['partial_range_of_motion'] if rep % 8 == 0 else []
        
        # Mock landmarks for AR
        mock_landmarks = [{'x': 0.5, 'y': 0.5, 'z': 0} for _ in range(33)]
        
        progress_response = wow_system.update_workout_progress(
            rep_count=rep,
            form_quality=form_quality,
            violations=violations,
            landmarks=mock_landmarks
        )
        
        # Print coaching responses
        if 'voice_coaching' in progress_response:
            coaching = progress_response['voice_coaching']
            if coaching['coaching_given']:
                for coach_msg in coaching['coaching_given']:
                    print(f"Rep {rep}: 🎤 {coach_msg['message']}")
        
        # Print talent insights
        if 'talent_insights' in progress_response:
            insights = progress_response['talent_insights']
            if insights.get('insights'):
                print(f"Rep {rep}: 💡 {insights['insights'][0]}")
        
        time.sleep(0.1)  # Simulate time between reps
    
    # End session and get comprehensive analysis
    print("\n🏁 Ending workout session...")
    session_summary = wow_system.end_workout_session(test_athlete)
    
    print(f"\n📋 SESSION SUMMARY:")
    print(f"Duration: {session_summary['duration_minutes']:.1f} minutes")
    print(f"Total Reps: {session_summary['total_reps']}")
    print(f"Average Form: {session_summary['average_form_quality']:.1f}%")
    print(f"Total Violations: {session_summary['total_violations']}")
    
    # Print talent analysis if available
    if session_summary['talent_analysis']:
        talent = session_summary['talent_analysis']['talent_prediction']
        print(f"\n🏆 TALENT ANALYSIS:")
        print(f"Level: {talent['level']}")
        print(f"Score: {talent['score']:.1f}/100")
        print(f"Percentile: {talent['percentile']:.1f}%")
    
    # Print Olympic assessment if available
    if session_summary['olympic_assessment']:
        olympic = session_summary['olympic_assessment']
        print(f"\n🥇 OLYMPIC ASSESSMENT:")
        print(f"Readiness Level: {olympic['readiness_level']}")
        print(f"Overall Score: {olympic['overall_score']:.1f}/100")
        print(f"Qualification Probability: {olympic['qualification_probability']:.1f}%")
    
    # Print recommendations
    print(f"\n💡 INTEGRATED RECOMMENDATIONS:")
    for i, rec in enumerate(session_summary['recommendations'][:4], 1):
        print(f"{i}. {rec}")
    
    # Test system status
    print(f"\n⚙️ SYSTEM STATUS:")
    status = wow_system.get_system_status()
    print(f"System Health: {status['system_health']}")
    print(f"Total Sessions: {status['total_sessions']}")
    print(f"Features Enabled: {len(status['enabled_features'])}")
    
    # Test language change
    print(f"\n🌐 Testing language change...")
    wow_system.change_language(Language.HINDI)
    print("Language changed to Hindi")
    
    # Cleanup
    time.sleep(2)
    wow_system.cleanup()
    
    print(f"\n{'='*60}")
    print("🎉 Integrated WOW Features System testing completed!")
    print(f"{'='*60}")



