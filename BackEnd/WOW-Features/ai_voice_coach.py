"""
AI Voice Coach System
Intelligent real-time coaching with multilingual support
"""

import pyttsx3
import threading
import time
import queue
from typing import Dict, List, Optional, Tuple
from enum import Enum
import random
import json

class Language(Enum):
    ENGLISH = "en"
    HINDI = "hi"

class CoachingType(Enum):
    MOTIVATION = "motivation"
    FORM_CORRECTION = "form_correction"
    REP_COUNT = "rep_count"
    ENCOURAGEMENT = "encouragement"
    WARNING = "warning"
    COMPLETION = "completion"

class AIVoiceCoach:
    """
    Advanced AI voice coaching system with real-time feedback
    """
    
    def __init__(self, language: Language = Language.ENGLISH, voice_speed: int = 150):
        self.language = language
        self.voice_speed = voice_speed
        
        # Initialize TTS engine
        self.tts_engine = pyttsx3.init()
        self.tts_engine.setProperty('rate', voice_speed)
        
        # Voice queue for async speaking
        self.voice_queue = queue.Queue()
        self.speaking_thread = None
        self.is_speaking = False
        
        # Coaching state
        self.last_coaching_time = 0
        self.coaching_interval = 3.0  # Minimum seconds between coaching
        self.rep_milestones = [5, 10, 15, 20, 25, 30]
        self.announced_milestones = set()
        
        # Load coaching phrases
        self.coaching_phrases = self._load_coaching_phrases()
        
        # Performance tracking
        self.last_rep_count = 0
        self.last_form_quality = 100
        self.consecutive_good_form = 0
        self.consecutive_poor_form = 0
        
        # Start voice thread
        self._start_voice_thread()
    
    def _load_coaching_phrases(self) -> Dict:
        """Load coaching phrases for different scenarios"""
        phrases = {
            Language.ENGLISH: {
                CoachingType.MOTIVATION: [
                    "Keep going! You're doing great!",
                    "Push through! You've got this!",
                    "Stay strong! Almost there!",
                    "Excellent work! Keep it up!",
                    "You're crushing it! Don't stop now!",
                    "Feel the burn! That's progress!",
                    "Outstanding effort! Keep pushing!"
                ],
                CoachingType.FORM_CORRECTION: [
                    "Focus on your form - quality over quantity",
                    "Slow down and control the movement",
                    "Keep your body aligned",
                    "Go deeper for full range of motion",
                    "Maintain proper posture",
                    "Control the negative movement",
                    "Engage your core muscles"
                ],
                CoachingType.REP_COUNT: [
                    "That's {count} reps! Keep going!",
                    "{count} down! You're making progress!",
                    "Excellent! {count} repetitions completed!",
                    "{count} reps and counting! Stay focused!",
                    "Great job! {count} reps done!"
                ],
                CoachingType.ENCOURAGEMENT: [
                    "You're stronger than you think!",
                    "Every rep makes you better!",
                    "Champions are made in moments like this!",
                    "Your dedication is inspiring!",
                    "Progress, not perfection!",
                    "You're building strength with every rep!"
                ],
                CoachingType.WARNING: [
                    "Watch your form - safety first!",
                    "Take a breath and reset your position",
                    "Quality reps are better than fast reps",
                    "Listen to your body",
                    "Form is breaking down - take a moment"
                ],
                CoachingType.COMPLETION: [
                    "Fantastic work! Session complete!",
                    "Outstanding performance! Well done!",
                    "You've crushed this workout!",
                    "Excellent session! You should be proud!",
                    "Mission accomplished! Great effort!"
                ]
            },
            Language.HINDI: {
                CoachingType.MOTIVATION: [
                    "Shabash! Aage badhte raho!",
                    "Himmat rakho! Tum kar sakte ho!",
                    "Majboot raho! Bas thoda aur!",
                    "Bahut accha! Aise hi karte raho!",
                    "Tum kamaal kar rahe ho!",
                    "Mehnat ka phal milega!",
                    "Excellent! Ruk mat jao!"
                ],
                CoachingType.FORM_CORRECTION: [
                    "Form par dhyan do - quality important hai",
                    "Dhire karo aur control rakho",
                    "Body ko straight rakho",
                    "Poora range of motion karo",
                    "Posture theek rakho",
                    "Control se karo",
                    "Core muscles tight rakho"
                ],
                CoachingType.REP_COUNT: [
                    "{count} reps ho gaye! Shabash!",
                    "{count} complete! Aage badhte raho!",
                    "Kamaal! {count} reps done!",
                    "{count} reps aur counting! Focus rakho!",
                    "Bahut badhiya! {count} reps complete!"
                ],
                CoachingType.ENCOURAGEMENT: [
                    "Tum bahut strong ho!",
                    "Har rep tumhe better banata hai!",
                    "Champions aise hi bante hain!",
                    "Tumhari mehnat inspiring hai!",
                    "Progress ho raha hai!",
                    "Strength badh rahi hai har rep se!"
                ],
                CoachingType.WARNING: [
                    "Form dekho - safety first!",
                    "Saans lo aur position reset karo",
                    "Quality reps better hain fast reps se",
                    "Body ki suno",
                    "Form bigad raha hai - thoda ruko"
                ],
                CoachingType.COMPLETION: [
                    "Kamaal ka workout! Complete ho gaya!",
                    "Outstanding performance! Shabash!",
                    "Tumne workout crush kar diya!",
                    "Excellent session! Proud feel karo!",
                    "Mission complete! Great effort!"
                ]
            }
        }
        
        return phrases
    
    def _start_voice_thread(self):
        """Start background thread for voice synthesis"""
        def voice_worker():
            while True:
                try:
                    message = self.voice_queue.get(timeout=1)
                    if message is None:  # Shutdown signal
                        break
                    
                    self.is_speaking = True
                    self.tts_engine.say(message)
                    self.tts_engine.runAndWait()
                    self.is_speaking = False
                    
                    self.voice_queue.task_done()
                except queue.Empty:
                    continue
                except Exception as e:
                    print(f"Voice synthesis error: {e}")
                    self.is_speaking = False
        
        self.speaking_thread = threading.Thread(target=voice_worker, daemon=True)
        self.speaking_thread.start()
    
    def provide_coaching(self, 
                        rep_count: int, 
                        form_quality: float, 
                        violations: List[str],
                        exercise_complete: bool = False,
                        timestamp: Optional[float] = None) -> Dict:
        """
        Provide intelligent coaching based on performance data
        
        Args:
            rep_count: Current repetition count
            form_quality: Form quality score (0-100)
            violations: List of current form violations
            exercise_complete: Whether exercise is complete
            timestamp: Current timestamp
            
        Returns:
            Dictionary with coaching information
        """
        if timestamp is None:
            timestamp = time.time()
        
        coaching_given = []
        
        # Check if enough time has passed for new coaching
        if timestamp - self.last_coaching_time < self.coaching_interval and not exercise_complete:
            return {'coaching_given': [], 'next_coaching_in': self.coaching_interval - (timestamp - self.last_coaching_time)}
        
        # Exercise completion coaching
        if exercise_complete:
            coaching_given.append(self._provide_completion_coaching(rep_count, form_quality))
        else:
            # Rep count milestones
            milestone_coaching = self._check_rep_milestones(rep_count)
            if milestone_coaching:
                coaching_given.append(milestone_coaching)
            
            # Form quality coaching
            form_coaching = self._analyze_form_coaching(form_quality, violations)
            if form_coaching:
                coaching_given.append(form_coaching)
            
            # Motivational coaching
            motivation_coaching = self._provide_motivational_coaching(rep_count, form_quality)
            if motivation_coaching:
                coaching_given.append(motivation_coaching)
        
        # Update tracking
        self.last_rep_count = rep_count
        self.last_form_quality = form_quality
        
        if coaching_given:
            self.last_coaching_time = timestamp
        
        return {
            'coaching_given': coaching_given,
            'next_coaching_in': self.coaching_interval,
            'is_speaking': self.is_speaking
        }
    
    def _check_rep_milestones(self, rep_count: int) -> Optional[Dict]:
        """Check and announce rep count milestones"""
        for milestone in self.rep_milestones:
            if rep_count >= milestone and milestone not in self.announced_milestones:
                self.announced_milestones.add(milestone)
                
                phrase = random.choice(self.coaching_phrases[self.language][CoachingType.REP_COUNT])
                message = phrase.format(count=milestone)
                
                self._speak_async(message)
                
                return {
                    'type': CoachingType.REP_COUNT.value,
                    'message': message,
                    'milestone': milestone
                }
        
        return None
    
    def _analyze_form_coaching(self, form_quality: float, violations: List[str]) -> Optional[Dict]:
        """Analyze form and provide coaching"""
        # Track consecutive form quality
        if form_quality >= 80:
            self.consecutive_good_form += 1
            self.consecutive_poor_form = 0
        else:
            self.consecutive_poor_form += 1
            self.consecutive_good_form = 0
        
        # Provide form correction if needed
        if form_quality < 70 or violations:
            if self.consecutive_poor_form >= 3:
                # Specific violation coaching
                if violations:
                    message = self._get_specific_violation_coaching(violations)
                else:
                    message = random.choice(self.coaching_phrases[self.language][CoachingType.FORM_CORRECTION])
                
                self._speak_async(message)
                
                return {
                    'type': CoachingType.FORM_CORRECTION.value,
                    'message': message,
                    'form_quality': form_quality,
                    'violations': violations
                }
        
        # Praise good form
        elif self.consecutive_good_form >= 5 and self.consecutive_good_form % 5 == 0:
            message = random.choice(self.coaching_phrases[self.language][CoachingType.ENCOURAGEMENT])
            self._speak_async(message)
            
            return {
                'type': CoachingType.ENCOURAGEMENT.value,
                'message': message,
                'form_quality': form_quality
            }
        
        return None
    
    def _get_specific_violation_coaching(self, violations: List[str]) -> str:
        """Get specific coaching for violations"""
        violation_coaching = {
            Language.ENGLISH: {
                'partial_range_of_motion': "Go deeper! Full range of motion!",
                'poor_form': "Focus on your technique!",
                'bouncing': "Control the movement - no bouncing!",
                'body_alignment': "Keep your body straight and aligned!",
                'speed_violation': "Slow down for better control!",
                'incomplete_rep': "Complete the full repetition!"
            },
            Language.HINDI: {
                'partial_range_of_motion': "Aur neeche jao! Poora range karo!",
                'poor_form': "Technique par focus karo!",
                'bouncing': "Movement control karo - bouncing nahi!",
                'body_alignment': "Body straight rakho!",
                'speed_violation': "Dhire karo better control ke liye!",
                'incomplete_rep': "Poora rep complete karo!"
            }
        }
        
        coaching_map = violation_coaching.get(self.language, violation_coaching[Language.ENGLISH])
        
        # Get coaching for first violation
        if violations:
            return coaching_map.get(violations[0], 
                                  random.choice(self.coaching_phrases[self.language][CoachingType.FORM_CORRECTION]))
        
        return random.choice(self.coaching_phrases[self.language][CoachingType.FORM_CORRECTION])
    
    def _provide_motivational_coaching(self, rep_count: int, form_quality: float) -> Optional[Dict]:
        """Provide motivational coaching"""
        # Random motivational coaching every 10-15 reps
        if rep_count > 0 and rep_count % random.randint(10, 15) == 0:
            message = random.choice(self.coaching_phrases[self.language][CoachingType.MOTIVATION])
            self._speak_async(message)
            
            return {
                'type': CoachingType.MOTIVATION.value,
                'message': message,
                'rep_count': rep_count
            }
        
        return None
    
    def _provide_completion_coaching(self, rep_count: int, avg_form_quality: float) -> Dict:
        """Provide completion coaching"""
        message = random.choice(self.coaching_phrases[self.language][CoachingType.COMPLETION])
        
        # Add performance summary
        if self.language == Language.ENGLISH:
            summary = f" You completed {rep_count} reps with {avg_form_quality:.0f}% average form quality!"
        else:
            summary = f" Tumne {rep_count} reps complete kiye {avg_form_quality:.0f}% average form quality ke saath!"
        
        full_message = message + summary
        self._speak_async(full_message)
        
        return {
            'type': CoachingType.COMPLETION.value,
            'message': full_message,
            'rep_count': rep_count,
            'form_quality': avg_form_quality
        }
    
    def _speak_async(self, message: str):
        """Add message to voice queue for async speaking"""
        if not self.voice_queue.full():
            self.voice_queue.put(message)
    
    def set_language(self, language: Language):
        """Change coaching language"""
        self.language = language
    
    def set_voice_speed(self, speed: int):
        """Set voice speed (words per minute)"""
        self.voice_speed = speed
        self.tts_engine.setProperty('rate', speed)
    
    def set_coaching_interval(self, interval: float):
        """Set minimum interval between coaching messages"""
        self.coaching_interval = interval
    
    def reset_session(self):
        """Reset for new exercise session"""
        self.announced_milestones.clear()
        self.last_rep_count = 0
        self.last_form_quality = 100
        self.consecutive_good_form = 0
        self.consecutive_poor_form = 0
        self.last_coaching_time = 0
    
    def get_coaching_stats(self) -> Dict:
        """Get coaching session statistics"""
        return {
            'language': self.language.value,
            'milestones_announced': len(self.announced_milestones),
            'consecutive_good_form': self.consecutive_good_form,
            'consecutive_poor_form': self.consecutive_poor_form,
            'is_speaking': self.is_speaking,
            'queue_size': self.voice_queue.qsize()
        }
    
    def cleanup(self):
        """Cleanup resources"""
        # Signal voice thread to stop
        self.voice_queue.put(None)
        if self.speaking_thread:
            self.speaking_thread.join(timeout=2)
        
        # Stop TTS engine
        try:
            self.tts_engine.stop()
        except:
            pass

# Example usage and testing
if __name__ == "__main__":
    # Test AI voice coach
    print("Testing AI Voice Coach System...")
    
    # Test English coaching
    english_coach = AIVoiceCoach(Language.ENGLISH, voice_speed=180)
    
    print("\n=== English Coaching Test ===")
    
    # Simulate workout session
    for rep in range(1, 21):
        # Simulate varying form quality
        form_quality = 85 + random.randint(-15, 10)
        violations = []
        
        # Add some violations for testing
        if form_quality < 75:
            violations = ['poor_form']
        
        coaching_result = english_coach.provide_coaching(
            rep_count=rep,
            form_quality=form_quality,
            violations=violations
        )
        
        if coaching_result['coaching_given']:
            for coaching in coaching_result['coaching_given']:
                print(f"Rep {rep}: [{coaching['type']}] {coaching['message']}")
        
        time.sleep(0.5)  # Simulate time between reps
    
    # Test completion
    completion_coaching = english_coach.provide_coaching(
        rep_count=20,
        form_quality=85,
        violations=[],
        exercise_complete=True
    )
    
    if completion_coaching['coaching_given']:
        for coaching in completion_coaching['coaching_given']:
            print(f"Completion: [{coaching['type']}] {coaching['message']}")
    
    # Get stats
    stats = english_coach.get_coaching_stats()
    print(f"\nCoaching Stats: {stats}")
    
    # Test Hindi coaching
    print("\n=== Hindi Coaching Test ===")
    hindi_coach = AIVoiceCoach(Language.HINDI, voice_speed=160)
    
    # Test milestone coaching
    hindi_coaching = hindi_coach.provide_coaching(
        rep_count=10,
        form_quality=90,
        violations=[]
    )
    
    if hindi_coaching['coaching_given']:
        for coaching in hindi_coaching['coaching_given']:
            print(f"Hindi: [{coaching['type']}] {coaching['message']}")
    
    # Test form correction
    form_coaching = hindi_coach.provide_coaching(
        rep_count=12,
        form_quality=60,
        violations=['partial_range_of_motion']
    )
    
    if form_coaching['coaching_given']:
        for coaching in form_coaching['coaching_given']:
            print(f"Hindi Form: [{coaching['type']}] {coaching['message']}")
    
    # Cleanup
    time.sleep(2)  # Wait for any pending speech
    english_coach.cleanup()
    hindi_coach.cleanup()
    
    print("\nAI Voice Coach testing completed!")

