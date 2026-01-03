import os
from dotenv import load_dotenv
from google import genai
from google.genai import types

load_dotenv()

client = genai.Client(api_key=os.getenv("GEMINI_API_KEY"))

response = client.models.generate_content(
    model="gemini-2.5-flash",
    contents="Explain how AI works in a few words",
)

print(response.text)

result = client.models.embed_content(
    model="gemini-embedding-001",
    contents=[
        "What is the meaning of life?",
        "What is the purpose of existence?"
    ]
)
print(result.embeddings)
# for embeding in result.embeddings:
#     print(embeding)
