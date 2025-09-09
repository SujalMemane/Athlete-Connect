Figma Screen Checklist

Auth & Routing
- Splash / Init
- Role Choice (Athlete | Coach/Org)
- Login (email/OTP/social)
- Permissions (camera/notifications) – Athlete only

Athlete Tabs
- Home/Dashboard (cards: next test, last results, top opportunity, chat snippet)
- Opportunities (List, Detail)
- Tests (List, Detail, Mode Select, Live Assessment, Results Summary)
- Messages (Conversation List, Chat Thread)
- Leaderboard (List, Player Detail)
- Achievements (optional)
- Profile, Settings

Edge/Error States
- No camera permission, Low light, Person not detected, Low FPS, Backend down

Components (variants)
- Camera Viewport; Metrics HUD Cards; Violation Toast; Voice Chip; Session Controls; Result Charts

Prototype Notes
- Session lifecycle: idle → starting → running → stopping → ended → idle
- Metrics cadence: 200–500 ms; Color thresholds: ≥80 success, 50–79 warn, <50 error

