"""
Flask API for Portfolio Voice Assistant RAG System
"""

from flask import Flask, request, jsonify
from flask_cors import CORS
import os
from rag_service import PortfolioRAG

# Initialize Flask app
app = Flask(__name__)
CORS(app)  # Enable CORS for all routes

# Global RAG service instance
rag_service = None


@app.before_request
def initialize_rag():
    """Initialize RAG service on first request"""
    global rag_service
    if rag_service is None:
        try:
            pdf_paths = [
                ("New_Resume.pdf", "My Resume"),
                ("Profile.pdf", "My Profile")
            ]
            rag_service = PortfolioRAG(pdf_paths=pdf_paths)
        except Exception as e:
            print(f"Error initializing RAG service: {e}")


@app.route('/', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({
        "status": "healthy",
        "service": "Portfolio Voice Assistant RAG API",
        "version": "1.0.0"
    }), 200


@app.route('/api/query', methods=['POST'])
def query_rag():
    """
    Query the RAG system and retrieve relevant text
    
    Expected JSON body:
    {
        "query": "Your question here"
    }
    """
    try:
        data = request.get_json()
        
        if not data or 'query' not in data:
            return jsonify({
                "error": "Missing 'query' field in request body"
            }), 400
        
        query_text = data['query'].strip()
        
        if not query_text:
            return jsonify({
                "error": "Query cannot be empty"
            }), 400
        
        # Query the RAG service (retrieval only)
        result = rag_service.query(query_text)
        
        return jsonify(result), 200
    
    except Exception as e:
        return jsonify({
            "error": str(e),
            "status": "error"
        }), 500


@app.route('/api/generate', methods=['POST'])
def generate_response():
    """
    Query the RAG system and generate a response using Gemini
    
    Expected JSON body:
    {
        "query": "Your question here",
        "model": "gemini-2.0-flash" (optional)
    }
    """
    try:
        data = request.get_json()
        
        if not data or 'query' not in data:
            return jsonify({
                "error": "Missing 'query' field in request body"
            }), 400
        
        query_text = data['query'].strip()
        model = data.get('model', 'gemini-2.0-flash')
        
        if not query_text:
            return jsonify({
                "error": "Query cannot be empty"
            }), 400
        
        # Query with generation
        result = rag_service.query_with_generation(query_text, model)
        
        return jsonify(result), 200
    
    except Exception as e:
        return jsonify({
            "error": str(e),
            "status": "error"
        }), 500


@app.route('/api/documents', methods=['GET'])
def get_documents():
    """Get list of loaded documents"""
    try:
        if rag_service is None or rag_service.documents is None:
            return jsonify({
                "documents": [],
                "count": 0
            }), 200
        
        documents = [
            {"title": doc["title"], "length": len(doc["text"])}
            for doc in rag_service.documents
        ]
        
        return jsonify({
            "documents": documents,
            "count": len(documents)
        }), 200
    
    except Exception as e:
        return jsonify({
            "error": str(e),
            "status": "error"
        }), 500


@app.route('/api/info', methods=['GET'])
def get_info():
    """Get API information"""
    return jsonify({
        "name": "Portfolio Voice Assistant RAG API",
        "version": "1.0.0",
        "description": "RAG-based voice assistant for portfolio queries with AI-generated responses",
        "endpoints": {
            "GET /": "Health check",
            "GET /api/info": "API information",
            "GET /api/documents": "List loaded documents",
            "POST /api/query": "Retrieve relevant documents (no generation)",
            "POST /api/generate": "Retrieve documents and generate AI response"
        }
    }), 200


@app.errorhandler(404)
def not_found(error):
    """Handle 404 errors"""
    return jsonify({
        "error": "Endpoint not found",
        "status": "error"
    }), 404


@app.errorhandler(500)
def internal_error(error):
    """Handle 500 errors"""
    return jsonify({
        "error": "Internal server error",
        "status": "error"
    }), 500


if __name__ == '__main__':
    # Get port from environment or default to 8080 (Cloud Run default)
    port = int(os.environ.get('PORT', 8080))
    app.run(host='0.0.0.0', port=port, debug=False)
