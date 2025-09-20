"""
AI Talent Prediction System
Machine Learning-based sports talent assessment and career guidance
"""

import numpy as np
import pandas as pd
from typing import Dict, List, Tuple, Optional
from enum import Enum
import json
import math
from sklearn.ensemble import RandomForestRegressor, GradientBoostingRegressor
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split
import joblib

class SportCategory(Enum):
    STRENGTH = "strength"
    ENDURANCE = "endurance"
    POWER = "power"
    AGILITY = "agility"
    FLEXIBILITY = "flexibility"
    COORDINATION = "coordination"

class TalentLevel(Enum):
    BEGINNER = "beginner"
    RECREATIONAL = "recreational"
    COMPETITIVE = "competitive"
    ELITE = "elite"
    OLYMPIC_POTENTIAL = "olympic_potential"

class TalentPredictor:
    """
    Advanced talent prediction system using machine learning
    """
    
    def __init__(self):
        # Initialize models
        self.talent_model = None
        self.sport_aptitude_model = None
        self.scaler = StandardScaler()
        
        # Sport categories and their requirements
        self.sport_requirements = self._define_sport_requirements()
        
        # Performance benchmarks
        self.benchmarks = self._load_performance_benchmarks()
        
        # Initialize models with synthetic data
        self._initialize_models()
        
    def _define_sport_requirements(self) -> Dict:
        """Define physical requirements for different sports"""
        return {
            'weightlifting': {
                'primary': [SportCategory.STRENGTH, SportCategory.POWER],
                'secondary': [SportCategory.COORDINATION],
                'weights': {'strength': 0.4, 'power': 0.3, 'endurance': 0.1, 'agility': 0.1, 'coordination': 0.1}
            },
            'marathon': {
                'primary': [SportCategory.ENDURANCE],
                'secondary': [SportCategory.STRENGTH],
                'weights': {'endurance': 0.6, 'strength': 0.2, 'power': 0.1, 'agility': 0.05, 'coordination': 0.05}
            },
            'sprinting': {
                'primary': [SportCategory.POWER, SportCategory.AGILITY],
                'secondary': [SportCategory.STRENGTH],
                'weights': {'power': 0.4, 'agility': 0.3, 'strength': 0.2, 'endurance': 0.05, 'coordination': 0.05}
            },
            'gymnastics': {
                'primary': [SportCategory.FLEXIBILITY, SportCategory.COORDINATION, SportCategory.STRENGTH],
                'secondary': [SportCategory.POWER],
                'weights': {'flexibility': 0.3, 'coordination': 0.3, 'strength': 0.2, 'power': 0.15, 'agility': 0.05}
            },
            'basketball': {
                'primary': [SportCategory.AGILITY, SportCategory.POWER, SportCategory.COORDINATION],
                'secondary': [SportCategory.ENDURANCE],
                'weights': {'agility': 0.25, 'power': 0.25, 'coordination': 0.25, 'endurance': 0.15, 'strength': 0.1}
            },
            'swimming': {
                'primary': [SportCategory.ENDURANCE, SportCategory.STRENGTH, SportCategory.COORDINATION],
                'secondary': [SportCategory.FLEXIBILITY],
                'weights': {'endurance': 0.35, 'strength': 0.25, 'coordination': 0.2, 'flexibility': 0.15, 'power': 0.05}
            },
            'boxing': {
                'primary': [SportCategory.POWER, SportCategory.AGILITY, SportCategory.ENDURANCE],
                'secondary': [SportCategory.COORDINATION],
                'weights': {'power': 0.3, 'agility': 0.25, 'endurance': 0.25, 'coordination': 0.15, 'strength': 0.05}
            }
        }
    
    def _load_performance_benchmarks(self) -> Dict:
        """Load performance benchmarks for different levels"""
        return {
            'pushups_1min': {
                TalentLevel.BEGINNER: 10,
                TalentLevel.RECREATIONAL: 25,
                TalentLevel.COMPETITIVE: 45,
                TalentLevel.ELITE: 65,
                TalentLevel.OLYMPIC_POTENTIAL: 80
            },
            'squats_1min': {
                TalentLevel.BEGINNER: 15,
                TalentLevel.RECREATIONAL: 35,
                TalentLevel.COMPETITIVE: 55,
                TalentLevel.ELITE: 75,
                TalentLevel.OLYMPIC_POTENTIAL: 90
            },
            'situps_1min': {
                TalentLevel.BEGINNER: 20,
                TalentLevel.RECREATIONAL: 40,
                TalentLevel.COMPETITIVE: 60,
                TalentLevel.ELITE: 80,
                TalentLevel.OLYMPIC_POTENTIAL: 100
            },
            'vertical_jump_cm': {
                TalentLevel.BEGINNER: 30,
                TalentLevel.RECREATIONAL: 45,
                TalentLevel.COMPETITIVE: 60,
                TalentLevel.ELITE: 75,
                TalentLevel.OLYMPIC_POTENTIAL: 85
            },
            'plank_seconds': {
                TalentLevel.BEGINNER: 30,
                TalentLevel.RECREATIONAL: 90,
                TalentLevel.COMPETITIVE: 180,
                TalentLevel.ELITE: 300,
                TalentLevel.OLYMPIC_POTENTIAL: 480
            }
        }
    
    def _initialize_models(self):
        """Initialize ML models with synthetic training data"""
        # Generate synthetic training data
        training_data = self._generate_training_data(1000)
        
        X = training_data[['strength', 'endurance', 'power', 'agility', 'flexibility', 'coordination',
                          'age', 'height', 'weight', 'training_years']]
        
        # Train talent level prediction model
        y_talent = training_data['talent_score']
        self.talent_model = GradientBoostingRegressor(n_estimators=100, random_state=42)
        self.talent_model.fit(X, y_talent)
        
        # Train sport aptitude model
        sport_scores = training_data[['weightlifting_score', 'marathon_score', 'sprinting_score', 
                                    'gymnastics_score', 'basketball_score', 'swimming_score', 'boxing_score']]
        self.sport_aptitude_model = RandomForestRegressor(n_estimators=100, random_state=42)
        self.sport_aptitude_model.fit(X, sport_scores)
        
        # Fit scaler
        self.scaler.fit(X)
    
    def _generate_training_data(self, n_samples: int) -> pd.DataFrame:
        """Generate synthetic training data for model training"""
        np.random.seed(42)
        
        data = []
        for _ in range(n_samples):
            # Generate basic attributes
            age = np.random.normal(25, 8)
            height = np.random.normal(170, 15)  # cm
            weight = np.random.normal(70, 15)   # kg
            training_years = np.random.exponential(3)
            
            # Generate physical attributes (0-100 scale)
            strength = max(0, min(100, np.random.normal(50, 20)))
            endurance = max(0, min(100, np.random.normal(50, 20)))
            power = max(0, min(100, np.random.normal(50, 20)))
            agility = max(0, min(100, np.random.normal(50, 20)))
            flexibility = max(0, min(100, np.random.normal(50, 20)))
            coordination = max(0, min(100, np.random.normal(50, 20)))
            
            # Calculate overall talent score
            talent_score = (strength + endurance + power + agility + flexibility + coordination) / 6
            talent_score += training_years * 2  # Training bonus
            talent_score = max(0, min(100, talent_score))
            
            # Calculate sport-specific scores
            sport_scores = {}
            for sport, requirements in self.sport_requirements.items():
                score = 0
                attributes = {
                    'strength': strength, 'endurance': endurance, 'power': power,
                    'agility': agility, 'flexibility': flexibility, 'coordination': coordination
                }
                
                for attr, weight in requirements['weights'].items():
                    score += attributes[attr] * weight
                
                sport_scores[f'{sport}_score'] = score
            
            # Add sample to dataset
            sample = {
                'age': age, 'height': height, 'weight': weight, 'training_years': training_years,
                'strength': strength, 'endurance': endurance, 'power': power,
                'agility': agility, 'flexibility': flexibility, 'coordination': coordination,
                'talent_score': talent_score,
                **sport_scores
            }
            data.append(sample)
        
        return pd.DataFrame(data)
    
    def analyze_performance(self, performance_data: Dict) -> Dict:
        """
        Analyze athletic performance and predict talent level
        
        Args:
            performance_data: Dictionary containing performance metrics
            
        Returns:
            Comprehensive talent analysis
        """
        # Extract physical attributes from performance
        physical_attributes = self._extract_physical_attributes(performance_data)
        
        # Calculate sport aptitudes
        sport_aptitudes = self._calculate_sport_aptitudes(physical_attributes)
        
        # Predict overall talent level
        talent_prediction = self._predict_talent_level(physical_attributes, performance_data)
        
        # Generate recommendations
        recommendations = self._generate_recommendations(physical_attributes, sport_aptitudes, talent_prediction)
        
        # Calculate improvement potential
        improvement_potential = self._calculate_improvement_potential(physical_attributes, performance_data)
        
        return {
            'physical_attributes': physical_attributes,
            'sport_aptitudes': sport_aptitudes,
            'talent_prediction': talent_prediction,
            'recommendations': recommendations,
            'improvement_potential': improvement_potential,
            'strengths': self._identify_strengths(physical_attributes),
            'areas_for_improvement': self._identify_weaknesses(physical_attributes),
            'career_guidance': self._provide_career_guidance(sport_aptitudes, talent_prediction)
        }
    
    def _extract_physical_attributes(self, performance_data: Dict) -> Dict:
        """Extract physical attributes from performance data"""
        attributes = {
            'strength': 50,      # Default values
            'endurance': 50,
            'power': 50,
            'agility': 50,
            'flexibility': 50,
            'coordination': 50
        }
        
        # Map performance metrics to attributes
        if 'pushups' in performance_data:
            pushup_score = min(100, (performance_data['pushups'] / 80) * 100)
            attributes['strength'] = pushup_score * 0.7 + attributes['strength'] * 0.3
        
        if 'squats' in performance_data:
            squat_score = min(100, (performance_data['squats'] / 90) * 100)
            attributes['strength'] = (attributes['strength'] + squat_score * 0.8) / 2
            attributes['power'] = squat_score * 0.6 + attributes['power'] * 0.4
        
        if 'situps' in performance_data:
            situp_score = min(100, (performance_data['situps'] / 100) * 100)
            attributes['endurance'] = situp_score * 0.6 + attributes['endurance'] * 0.4
            attributes['strength'] = (attributes['strength'] + situp_score * 0.4) / 2
        
        if 'vertical_jump' in performance_data:
            jump_score = min(100, (performance_data['vertical_jump'] / 85) * 100)
            attributes['power'] = jump_score * 0.8 + attributes['power'] * 0.2
            attributes['agility'] = jump_score * 0.5 + attributes['agility'] * 0.5
        
        if 'plank_time' in performance_data:
            plank_score = min(100, (performance_data['plank_time'] / 480) * 100)
            attributes['endurance'] = (attributes['endurance'] + plank_score * 0.7) / 2
            attributes['strength'] = (attributes['strength'] + plank_score * 0.3) / 2
        
        # Factor in form quality
        if 'average_form_quality' in performance_data:
            form_factor = performance_data['average_form_quality'] / 100
            attributes['coordination'] = attributes['coordination'] * 0.5 + (form_factor * 100) * 0.5
        
        # Factor in consistency
        if 'consistency_score' in performance_data:
            consistency_factor = performance_data['consistency_score'] / 100
            for attr in attributes:
                attributes[attr] *= (0.8 + consistency_factor * 0.2)
        
        return attributes
    
    def _calculate_sport_aptitudes(self, physical_attributes: Dict) -> Dict:
        """Calculate aptitude scores for different sports"""
        aptitudes = {}
        
        for sport, requirements in self.sport_requirements.items():
            score = 0
            for attr, weight in requirements['weights'].items():
                if attr in physical_attributes:
                    score += physical_attributes[attr] * weight
            
            aptitudes[sport] = min(100, max(0, score))
        
        return aptitudes
    
    def _predict_talent_level(self, physical_attributes: Dict, performance_data: Dict) -> Dict:
        """Predict overall talent level using ML model"""
        # Prepare features for prediction
        features = [
            physical_attributes.get('strength', 50),
            physical_attributes.get('endurance', 50),
            physical_attributes.get('power', 50),
            physical_attributes.get('agility', 50),
            physical_attributes.get('flexibility', 50),
            physical_attributes.get('coordination', 50),
            performance_data.get('age', 25),
            performance_data.get('height', 170),
            performance_data.get('weight', 70),
            performance_data.get('training_years', 1)
        ]
        
        # Scale features
        features_scaled = self.scaler.transform([features])
        
        # Predict talent score
        talent_score = self.talent_model.predict(features_scaled)[0]
        talent_score = max(0, min(100, talent_score))
        
        # Determine talent level
        if talent_score >= 90:
            level = TalentLevel.OLYMPIC_POTENTIAL
        elif talent_score >= 80:
            level = TalentLevel.ELITE
        elif talent_score >= 65:
            level = TalentLevel.COMPETITIVE
        elif talent_score >= 45:
            level = TalentLevel.RECREATIONAL
        else:
            level = TalentLevel.BEGINNER
        
        return {
            'score': talent_score,
            'level': level.value,
            'percentile': self._calculate_percentile(talent_score),
            'confidence': min(100, talent_score + 10)  # Confidence in prediction
        }
    
    def _calculate_percentile(self, score: float) -> float:
        """Calculate percentile ranking"""
        # Assume normal distribution with mean=50, std=20
        from scipy import stats
        percentile = stats.norm.cdf(score, loc=50, scale=20) * 100
        return min(99, max(1, percentile))
    
    def _identify_strengths(self, physical_attributes: Dict) -> List[Dict]:
        """Identify top physical strengths"""
        strengths = []
        sorted_attrs = sorted(physical_attributes.items(), key=lambda x: x[1], reverse=True)
        
        for attr, score in sorted_attrs[:3]:  # Top 3 strengths
            if score >= 60:  # Only include if above average
                strengths.append({
                    'attribute': attr.replace('_', ' ').title(),
                    'score': score,
                    'level': self._get_attribute_level(score)
                })
        
        return strengths
    
    def _identify_weaknesses(self, physical_attributes: Dict) -> List[Dict]:
        """Identify areas needing improvement"""
        weaknesses = []
        sorted_attrs = sorted(physical_attributes.items(), key=lambda x: x[1])
        
        for attr, score in sorted_attrs[:2]:  # Bottom 2 attributes
            if score < 50:  # Only include if below average
                weaknesses.append({
                    'attribute': attr.replace('_', ' ').title(),
                    'score': score,
                    'improvement_needed': 50 - score,
                    'training_focus': self._get_training_focus(attr)
                })
        
        return weaknesses
    
    def _get_attribute_level(self, score: float) -> str:
        """Get descriptive level for attribute score"""
        if score >= 85:
            return "Exceptional"
        elif score >= 75:
            return "Excellent"
        elif score >= 65:
            return "Good"
        elif score >= 50:
            return "Average"
        else:
            return "Below Average"
    
    def _get_training_focus(self, attribute: str) -> str:
        """Get training recommendations for specific attribute"""
        training_map = {
            'strength': "Focus on resistance training, weight lifting, and bodyweight exercises",
            'endurance': "Incorporate cardio, long-distance running, and circuit training",
            'power': "Practice explosive movements, plyometrics, and sprint training",
            'agility': "Work on ladder drills, cone exercises, and change of direction training",
            'flexibility': "Regular stretching, yoga, and mobility work",
            'coordination': "Balance exercises, sport-specific drills, and motor skill practice"
        }
        return training_map.get(attribute, "Consult with a sports trainer for specific guidance")
    
    def _generate_recommendations(self, physical_attributes: Dict, sport_aptitudes: Dict, talent_prediction: Dict) -> List[str]:
        """Generate personalized recommendations"""
        recommendations = []
        
        # Talent level recommendations
        level = talent_prediction['level']
        if level == TalentLevel.OLYMPIC_POTENTIAL.value:
            recommendations.append("Consider professional coaching and elite training programs")
            recommendations.append("Explore opportunities for national team selection")
        elif level == TalentLevel.ELITE.value:
            recommendations.append("Pursue competitive sports at regional/national level")
            recommendations.append("Consider specialized coaching and training facilities")
        elif level == TalentLevel.COMPETITIVE.value:
            recommendations.append("Join competitive sports clubs and participate in tournaments")
            recommendations.append("Focus on consistent training and skill development")
        else:
            recommendations.append("Build fundamental fitness through regular training")
            recommendations.append("Explore different sports to find your passion")
        
        # Sport-specific recommendations
        best_sports = sorted(sport_aptitudes.items(), key=lambda x: x[1], reverse=True)[:2]
        for sport, score in best_sports:
            if score >= 65:
                recommendations.append(f"Strong aptitude for {sport.replace('_', ' ').title()} - consider specializing")
        
        # Attribute-based recommendations
        strengths = self._identify_strengths(physical_attributes)
        if strengths:
            top_strength = strengths[0]['attribute']
            recommendations.append(f"Leverage your {top_strength} in training and sport selection")
        
        return recommendations
    
    def _calculate_improvement_potential(self, physical_attributes: Dict, performance_data: Dict) -> Dict:
        """Calculate potential for improvement"""
        current_avg = sum(physical_attributes.values()) / len(physical_attributes)
        
        # Factors affecting improvement potential
        age_factor = 1.0
        if 'age' in performance_data:
            age = performance_data['age']
            if age < 20:
                age_factor = 1.3  # High potential for young athletes
            elif age < 30:
                age_factor = 1.1
            elif age > 35:
                age_factor = 0.8
        
        training_factor = 1.0
        if 'training_years' in performance_data:
            years = performance_data['training_years']
            if years < 2:
                training_factor = 1.4  # High potential for beginners
            elif years < 5:
                training_factor = 1.2
            else:
                training_factor = 0.9
        
        # Calculate potential score
        potential_score = min(100, current_avg * age_factor * training_factor)
        improvement_potential = potential_score - current_avg
        
        return {
            'current_level': current_avg,
            'potential_level': potential_score,
            'improvement_potential': max(0, improvement_potential),
            'timeline_months': max(6, int(improvement_potential * 2)),  # Estimated timeline
            'confidence': min(100, 70 + (improvement_potential * 2))
        }
    
    def _provide_career_guidance(self, sport_aptitudes: Dict, talent_prediction: Dict) -> Dict:
        """Provide career guidance based on analysis"""
        best_sports = sorted(sport_aptitudes.items(), key=lambda x: x[1], reverse=True)
        talent_level = talent_prediction['level']
        
        guidance = {
            'primary_sports': [],
            'secondary_sports': [],
            'career_paths': [],
            'timeline': {}
        }
        
        # Primary sport recommendations
        for sport, score in best_sports[:2]:
            if score >= 70:
                guidance['primary_sports'].append({
                    'sport': sport.replace('_', ' ').title(),
                    'aptitude_score': score,
                    'potential_level': self._get_sport_potential_level(score, talent_level)
                })
        
        # Secondary sports
        for sport, score in best_sports[2:4]:
            if score >= 60:
                guidance['secondary_sports'].append({
                    'sport': sport.replace('_', ' ').title(),
                    'aptitude_score': score
                })
        
        # Career path recommendations
        if talent_level in [TalentLevel.ELITE.value, TalentLevel.OLYMPIC_POTENTIAL.value]:
            guidance['career_paths'] = [
                "Professional Athlete",
                "National Team Member",
                "Sports Coach/Trainer",
                "Sports Science Researcher"
            ]
        elif talent_level == TalentLevel.COMPETITIVE.value:
            guidance['career_paths'] = [
                "Competitive Athlete",
                "Sports Coach",
                "Fitness Trainer",
                "Sports Administrator"
            ]
        else:
            guidance['career_paths'] = [
                "Recreational Sports",
                "Fitness Instructor",
                "Sports Enthusiast",
                "Youth Sports Coach"
            ]
        
        # Timeline recommendations
        guidance['timeline'] = {
            'short_term_6months': "Focus on fundamental training and skill development",
            'medium_term_2years': "Participate in competitions and seek specialized coaching",
            'long_term_5years': "Pursue advanced training and consider professional opportunities"
        }
        
        return guidance
    
    def _get_sport_potential_level(self, aptitude_score: float, talent_level: str) -> str:
        """Get potential level for specific sport"""
        base_potential = "recreational"
        
        if aptitude_score >= 85 and talent_level in [TalentLevel.ELITE.value, TalentLevel.OLYMPIC_POTENTIAL.value]:
            base_potential = "professional"
        elif aptitude_score >= 75 and talent_level in [TalentLevel.COMPETITIVE.value, TalentLevel.ELITE.value]:
            base_potential = "competitive"
        elif aptitude_score >= 65:
            base_potential = "semi-competitive"
        
        return base_potential
    
    def save_model(self, filepath: str):
        """Save trained models to file"""
        model_data = {
            'talent_model': self.talent_model,
            'sport_aptitude_model': self.sport_aptitude_model,
            'scaler': self.scaler,
            'sport_requirements': self.sport_requirements,
            'benchmarks': self.benchmarks
        }
        joblib.dump(model_data, filepath)
    
    def load_model(self, filepath: str):
        """Load trained models from file"""
        model_data = joblib.load(filepath)
        self.talent_model = model_data['talent_model']
        self.sport_aptitude_model = model_data['sport_aptitude_model']
        self.scaler = model_data['scaler']
        self.sport_requirements = model_data['sport_requirements']
        self.benchmarks = model_data['benchmarks']

# Example usage and testing
if __name__ == "__main__":
    print("Testing AI Talent Prediction System...")
    
    # Initialize predictor
    predictor = TalentPredictor()
    
    # Test with sample performance data
    test_cases = [
        {
            'name': 'Elite Athlete',
            'data': {
                'pushups': 70,
                'squats': 85,
                'situps': 90,
                'vertical_jump': 80,
                'plank_time': 400,
                'average_form_quality': 92,
                'consistency_score': 88,
                'age': 24,
                'height': 175,
                'weight': 72,
                'training_years': 6
            }
        },
        {
            'name': 'Recreational Athlete',
            'data': {
                'pushups': 35,
                'squats': 45,
                'situps': 50,
                'vertical_jump': 50,
                'plank_time': 120,
                'average_form_quality': 78,
                'consistency_score': 72,
                'age': 28,
                'height': 170,
                'weight': 75,
                'training_years': 2
            }
        },
        {
            'name': 'Beginner',
            'data': {
                'pushups': 15,
                'squats': 25,
                'situps': 30,
                'vertical_jump': 35,
                'plank_time': 45,
                'average_form_quality': 65,
                'consistency_score': 60,
                'age': 32,
                'height': 168,
                'weight': 80,
                'training_years': 0.5
            }
        }
    ]
    
    for test_case in test_cases:
        print(f"\n{'='*60}")
        print(f"ANALYSIS FOR: {test_case['name']}")
        print(f"{'='*60}")
        
        analysis = predictor.analyze_performance(test_case['data'])
        
        print(f"\n🏆 TALENT PREDICTION:")
        talent = analysis['talent_prediction']
        print(f"  Level: {talent['level'].upper()}")
        print(f"  Score: {talent['score']:.1f}/100")
        print(f"  Percentile: {talent['percentile']:.1f}%")
        print(f"  Confidence: {talent['confidence']:.1f}%")
        
        print(f"\n💪 PHYSICAL ATTRIBUTES:")
        for attr, score in analysis['physical_attributes'].items():
            print(f"  {attr.title()}: {score:.1f}")
        
        print(f"\n🏅 TOP SPORT APTITUDES:")
        sorted_sports = sorted(analysis['sport_aptitudes'].items(), key=lambda x: x[1], reverse=True)
        for sport, score in sorted_sports[:3]:
            print(f"  {sport.replace('_', ' ').title()}: {score:.1f}")
        
        print(f"\n⭐ STRENGTHS:")
        for strength in analysis['strengths']:
            print(f"  {strength['attribute']}: {strength['score']:.1f} ({strength['level']})")
        
        print(f"\n🎯 AREAS FOR IMPROVEMENT:")
        for weakness in analysis['areas_for_improvement']:
            print(f"  {weakness['attribute']}: {weakness['score']:.1f}")
            print(f"    Training: {weakness['training_focus']}")
        
        print(f"\n📈 IMPROVEMENT POTENTIAL:")
        potential = analysis['improvement_potential']
        print(f"  Current Level: {potential['current_level']:.1f}")
        print(f"  Potential Level: {potential['potential_level']:.1f}")
        print(f"  Improvement: +{potential['improvement_potential']:.1f} points")
        print(f"  Timeline: {potential['timeline_months']} months")
        
        print(f"\n💡 RECOMMENDATIONS:")
        for rec in analysis['recommendations'][:3]:
            print(f"  • {rec}")
        
        print(f"\n🚀 CAREER GUIDANCE:")
        career = analysis['career_guidance']
        if career['primary_sports']:
            print(f"  Primary Sports: {', '.join([s['sport'] for s in career['primary_sports']])}")
        print(f"  Career Paths: {', '.join(career['career_paths'][:2])}")
    
    print(f"\n{'='*60}")
    print("AI Talent Prediction System testing completed!")
    print(f"{'='*60}")

