from flask import Flask, request, jsonify
from flask_cors import CORS
import uuid

app = Flask(__name__)
CORS(app)

_sessions = {}

@app.get('/')
def index():
    return jsonify({ 'status': 'ok', 'service': 'sports-assessment-backend' })

@app.get('/health')
def health():
    return jsonify({ 'ok': True })

@app.post('/assessment/start')
def start_assessment():
    data = request.get_json() or {}
    session_id = str(uuid.uuid4())
    _sessions[session_id] = { 'test': data }
    return jsonify({ 'sessionId': session_id, 'message': f"Started {data.get('testName')}" })

@app.post('/assessment/analyze')
def analyze():
    data = request.get_json() or {}
    # TODO: integrate with integrated_ai_system if desired
    return jsonify({
        'percentile': 80,
        'feedback': f"Analysis complete for {data.get('testName')}",
        'recommendations': ['Maintain form', 'Increase intensity gradually'],
        'talentScore': 0.72,
        'olympicReadiness': 'Developing'
    })

@app.post('/assessment/stop')
def stop_assessment():
    data = request.get_json() or {}
    sid = data.get('sessionId')
    _sessions.pop(sid, None)
    return jsonify({ 'message': 'Stopped' })

if __name__ == '__main__':
    # Bind to all interfaces so the Android emulator (10.0.2.2) can reach it
    app.run(host='0.0.0.0', port=5000)


