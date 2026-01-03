"""
RAG (Retrieval-Augmented Generation) Service Module
Handles document embedding and query retrieval
"""

import os
import numpy as np
import pandas as pd
from pypdf import PdfReader
from google import genai
from google.genai import types
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

# Initialize Google Generative AI client
client = genai.Client(api_key=os.getenv("GEMINI_API_KEY"))

# Constants
EMBEDDING_MODEL_ID = "gemini-embedding-001"


def extract_pdf_text(pdf_path: str) -> str:
    """Extract all text from a PDF file"""
    reader = PdfReader(pdf_path)
    text = ""
    for page in reader.pages:
        text += page.extract_text() + "\n"
    return text.strip()


def create_documents_from_pdfs(pdf_paths: list) -> list:
    """
    Create document dictionaries from PDF files
    
    Args:
        pdf_paths: List of tuples (pdf_path, title)
        
    Returns:
        List of document dictionaries with text
    """
    documents = []
    for pdf_path, title in pdf_paths:
        text = extract_pdf_text(pdf_path)
        documents.append({
            "title": title,
            "text": text
        })
    return documents


def embed_documents(documents: list) -> tuple:
    """
    Embed documents and return dataframe with embeddings
    
    Args:
        documents: List of document dictionaries
        
    Returns:
        Tuple of (dataframe, embeddings_list)
    """
    embeddings_list = []
    
    for doc in documents:
        embedding = client.models.embed_content(
            model=EMBEDDING_MODEL_ID,
            contents=doc["text"],
            config=types.EmbedContentConfig(
                task_type="retrieval_document",
                title=doc["title"]
            )
        )
        doc["embedding"] = embedding.embeddings[0].values
        embeddings_list.append(embedding.embeddings[0].values)
        print(f"✓ {doc['title']} embedded successfully")
    
    # Create dataframe with embeddings
    df = pd.DataFrame({
        'Title': [doc['title'] for doc in documents],
        'Text': [doc['text'] for doc in documents],
        'Embeddings': [doc['embedding'] for doc in documents]
    })
    
    return df, embeddings_list


def find_best_passage(query: str, dataframe: pd.DataFrame) -> str:
    """
    Find the most relevant passage in the dataframe based on the query
    using cosine similarity via dot product
    
    Args:
        query: User query string
        dataframe: DataFrame with document embeddings
        
    Returns:
        The most relevant document text
    """
    # Embed the query
    query_embedding = client.models.embed_content(
        model=EMBEDDING_MODEL_ID,
        contents=query,
        config=types.EmbedContentConfig(
            task_type="retrieval_document",
        )
    )
    
    # Calculate dot products (similarity scores)
    dot_products = np.dot(
        np.stack(dataframe['Embeddings']),
        query_embedding.embeddings[0].values
    )
    
    # Return the most relevant document
    idx = np.argmax(dot_products)
    return dataframe.iloc[idx]['Text']


def generate_response(query: str, context: str, model: str = "gemini-2.0-flash") -> str:
    """
    Generate a natural response using Gemini model with retrieved context
    
    Args:
        query: User query
        context: Retrieved context from documents
        model: Gemini model to use
        
    Returns:
        Generated response text
    """
    prompt = f"""You are a helpful assistant answering questions about Clency Christine's portfolio and experience.

Use the following context from the portfolio to answer the question:

CONTEXT:
{context}

QUESTION: {query}

Please provide a clear, concise, and helpful answer based on the context provided. If the context doesn't contain relevant information, acknowledge that politely."""

    response = client.models.generate_content(
        model=model,
        contents=prompt
    )
    
    return response.text


class PortfolioRAG:
    """Main RAG Service Class"""
    
    def __init__(self, pdf_paths: list = None):
        """
        Initialize the RAG service
        
        Args:
            pdf_paths: List of tuples (pdf_path, title) for documents
        """
        self.dataframe = None
        self.documents = None
        
        if pdf_paths:
            self.load_documents(pdf_paths)
    
    def load_documents(self, pdf_paths: list):
        """Load and embed documents from PDF files"""
        self.documents = create_documents_from_pdfs(pdf_paths)
        self.dataframe, _ = embed_documents(self.documents)
        print(f"\nLoaded and embedded {len(self.documents)} documents")
    
    def query(self, query_text: str) -> dict:
        """
        Query the RAG system and retrieve relevant text
        
        Args:
            query_text: User query
            
        Returns:
            Dictionary with query and retrieved text
        """
        if self.dataframe is None:
            raise ValueError("No documents loaded. Call load_documents() first.")
        
        retrieved_text = find_best_passage(query_text, self.dataframe)
        
        return {
            "query": query_text,
            "retrieved_text": retrieved_text,
            "status": "success"
        }
    
    def query_with_generation(self, query_text: str, model: str = "gemini-2.0-flash") -> dict:
        """
        Query the RAG system and generate a response using Gemini
        
        Args:
            query_text: User query
            model: Gemini model to use for generation
            
        Returns:
            Dictionary with query, retrieved context, and generated response
        """
        if self.dataframe is None:
            raise ValueError("No documents loaded. Call load_documents() first.")
        
        # Retrieve relevant context
        context = find_best_passage(query_text, self.dataframe)
        
        # Generate response using the context
        response = generate_response(query_text, context, model)
        
        return {
            "query": query_text,
            "context": context,
            "response": response,
            "status": "success"
        }


# Initialize default RAG service
def get_rag_service() -> PortfolioRAG:
    """Get or create the RAG service instance"""
    rag = PortfolioRAG(pdf_paths=[
        ("New_Resume.pdf", "My Resume"),
        ("Profile.pdf", "My Profile")
    ])
    return rag
