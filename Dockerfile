# Use Python 3.13 slim image
FROM python:3.13-slim

# Set working directory
WORKDIR /app

# Install build dependencies needed for numpy and other packages
RUN apt-get update && apt-get install -y \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Copy requirements first for better caching
COPY requirements.txt .

# Install Python dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy application files
COPY app.py .
COPY rag_service.py .
COPY .env .
COPY New_Resume.pdf .
COPY Profile.pdf .

# Expose port (Cloud Run uses 8080)
EXPOSE 8080

# Set environment variable for Python
ENV PYTHONUNBUFFERED=True

# Run the application
CMD ["python", "app.py"]
