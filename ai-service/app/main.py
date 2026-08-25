from fastapi import FastAPI, HTTPException, Query
from fastapi.middleware.cors import CORSMiddleware
from typing import Optional

from app.config import settings
from app.schemas import (
    ChatRequest, ChatResponse,
    TipResponse,
    RagAskRequest, RagAskResponse,
    NewsItem, NewsResponse
)
from app.services.groq_service import groq_service
from app.services.rag_service import rag_service

app = FastAPI(
    title="MineWiki AI Microservice",
    description="Microservicio de Inteligencia Artificial para la app MineWiki usando Groq API y RAG",
    version="1.0.0"
)

# Configurar CORS para permitir peticiones desde la app Android y el servidor Ktor
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
def read_root():
    return {
        "service": "MineWiki AI Microservice",
        "status": "running",
        "groq_configured": groq_service.is_configured(),
        "docs_url": "/docs"
    }

@app.get("/health")
def health_check():
    return {
        "status": "healthy",
        "groq_status": "connected" if groq_service.is_configured() else "mock_mode"
    }

@app.post("/api/v1/chat", response_model=ChatResponse)
def chat_with_ai(request: ChatRequest):
    """
    Endpoint para chatear con el Asistente Experto de Minecraft powered by Groq.
    Soporta historial de conversacion para control de contexto multi-turno.
    """
    if not request.message.strip():
        raise HTTPException(status_code=400, detail="El mensaje no puede estar vacío.")

    # Convertir historial de Pydantic a lista de dicts para el servicio
    history_dicts = None
    if request.history:
        history_dicts = [{"role": msg.role, "content": msg.content} for msg in request.history]

    result = groq_service.generate_chat_reply(
        message=request.message,
        use_fast_model=request.use_fast_model,
        context=request.context,
        history=history_dicts
    )

    return ChatResponse(
        reply=result["reply"],
        model_used=result["model_used"],
        is_mock=result["is_mock"],
        usage=result.get("usage")
    )

@app.get("/api/v1/tip", response_model=TipResponse)
def get_dynamic_tip(category: Optional[str] = Query(None, description="Categoría opcional del consejo")):
    """
    Genera un consejo o tip dinámico de Minecraft en tiempo real usando IA.
    """
    result = groq_service.generate_dynamic_tip(category=category)
    return TipResponse(
        tip=result["tip"],
        category=result["category"],
        source=result["source"]
    )

@app.post("/api/v1/rag/ask", response_model=RagAskResponse)
def ask_rag_wiki(request: RagAskRequest):
    """
    Consulta la Wiki de Minecraft utilizando RAG (Retrieval-Augmented Generation).
    Recupera los artículos más relevantes y responde con base en la documentación oficial.
    """
    if not request.question.strip():
        raise HTTPException(status_code=400, detail="La pregunta no puede estar vacía.")
    
    result = rag_service.ask_rag(question=request.question, top_k=request.top_k)
    return RagAskResponse(
        answer=result["answer"],
        sources=result["sources"],
        model_used=result["model_used"]
    )

@app.get("/api/v1/news", response_model=NewsResponse)
def get_minecraft_news():
    """
    Obtiene las noticias destacadas de Minecraft (Nuevas versiones, Snapshots, Biomas) impulsadas por la IA.
    """
    result = groq_service.generate_minecraft_news()
    return NewsResponse(
        news=result["news"],
        source=result["source"]
    )

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app.main:app", host=settings.HOST, port=settings.PORT, reload=True)
