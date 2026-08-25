from pydantic import BaseModel, Field
from typing import Optional, List

class ChatMessage(BaseModel):
    role: str = Field(..., description="Rol del mensaje: 'user' o 'assistant'")
    content: str = Field(..., description="Contenido del mensaje")

class ChatRequest(BaseModel):
    message: str = Field(..., description="Pregunta o consulta del usuario sobre Minecraft")
    use_fast_model: bool = Field(default=True, description="Si es True usa modelo rapido, sino modelo avanzado")
    context: Optional[str] = Field(default=None, description="Contexto opcional del estado del juego")
    history: Optional[List[ChatMessage]] = Field(default=None, description="Historial de mensajes anteriores para control de contexto")

class TokenUsage(BaseModel):
    prompt_tokens: int = Field(default=0, description="Tokens consumidos por el prompt/contexto de entrada")
    completion_tokens: int = Field(default=0, description="Tokens consumidos por la respuesta de la IA")
    total_tokens: int = Field(default=0, description="Tokens totales consumidos en la petición")

class ChatResponse(BaseModel):
    reply: str
    model_used: str
    is_mock: bool = False
    usage: Optional[TokenUsage] = None

class TipRequest(BaseModel):
    category: Optional[str] = Field(default=None, description="Categoría del consejo (ej: supervivencia, nether, redstone)")

class TipResponse(BaseModel):
    tip: str
    category: str
    source: str

class RagAskRequest(BaseModel):
    question: str = Field(..., description="Pregunta a consultar en la Wiki vectorial")
    top_k: int = Field(default=3, description="Número de fuentes a recuperar")

class SourceDoc(BaseModel):
    title: str
    content: str
    score: float

class RagAskResponse(BaseModel):
    answer: str
    sources: List[SourceDoc]
    model_used: str
    docs_retrieved: int = 0

class NewsItem(BaseModel):
    id: int
    title: str
    tag: str
    version: str
    summary: str
    date: str

class NewsResponse(BaseModel):
    news: List[NewsItem]
    source: str
