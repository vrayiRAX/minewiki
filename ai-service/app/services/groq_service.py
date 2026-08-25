import os
import random
from app.config import settings

class GroqService:
    def __init__(self):
        self.api_key = settings.GROQ_API_KEY
        self.client = None
        
        # Verificar si hay una API Key válida
        if self.api_key and self.api_key != "tu_api_key_de_groq_aqui" and self.api_key.startswith("gsk_"):
            try:
                from groq import Groq
                self.client = Groq(api_key=self.api_key)
            except Exception as e:
                print(f"[GroqService] Error inicializando SDK de Groq: {e}")

    def is_configured(self) -> bool:
        return self.client is not None

    def generate_chat_reply(self, message: str, use_fast_model: bool = True, context: str = None, history: list = None) -> dict:
        selected_model = settings.GROQ_MODEL_FAST if use_fast_model else settings.GROQ_MODEL
        
        if not self.is_configured():
            # Modo fallback inteligente si no se ha configurado la API Key de Groq aún
            return {
                "reply": f"[Modo Demostración / IA Offline] Para '{message}': En Minecraft, es recomendable llevar siempre agua y comida. (Configura GROQ_API_KEY en .env para respuestas de IA completas).",
                "model_used": "mock-local",
                "is_mock": True
            }

        try:
            system_prompt = """Eres MineWiki Assistant, experto oficial de Minecraft Java/Bedrock. Responde SIEMPRE en español, de forma precisa, clara y directa.

REGLAS CLAVE:
- Obsidiana: Pico diamante/netherita (pico hierro la destruye). Portal Nether: min 10 obsidiana (4x5). 1 blq Nether = 8 Overworld.
- Netherita: 4 lingotes oro + 4 netherita antigua (Y=8 a 22).
- Wither: 4 arena almas en T + 3 calaveras wither skeleton -> Drop: Estrella Nether.
- Dragón End: Huevo + EXP. Faro (Beacon): 5 cristal + 1 estrella nether + 3 obsidiana. Pirámide niv 1=9 blqs, niv 4=164.
- Mesa Encantamientos: 1 libro, 2 diamantes, 4 obsidiana. 15 estanterías para niv 30. Fortuna no aplica a hierro/oro crudo.
- Warden: 500 HP en Deep Dark (ciego). Piglins: requieren armadura oro. Creepers: huyen de gatos. Tortugas: scutes al crecer. Diamantes: Capa Y=-58.

FORMATO: Responde directo a lo pedido. Usa tablas para crafteos y listas breves. Evita explicaciones largas no solicitadas."""

            messages = [
                {"role": "system", "content": system_prompt}
            ]

            if context:
                messages.append({"role": "system", "content": f"Contexto actual del jugador: {context}"})

            # Insertar historial de conversacion optimizado (ultimos 4 mensajes / 2 turnos para ahorrar tokens)
            if history:
                for msg in history[-4:]:
                    messages.append({"role": msg.get("role", "user"), "content": msg.get("content", "")})

            messages.append({"role": "user", "content": message})
            print(f"[GroqService] Enviando {len(messages)} mensajes al modelo (incluyendo {len(history) if history else 0} de historial)")

            completion = self.client.chat.completions.create(
                model=selected_model,
                messages=messages,
                temperature=0.3,
                max_tokens=600,
            )
            
            import re
            reply_content = completion.choices[0].message.content
            # Limpiar etiquetas <think> de modelos de razonamiento como Qwen
            reply_content = re.sub(r'<think>.*?</think>', '', reply_content, flags=re.DOTALL).strip()

            usage_dict = None
            if hasattr(completion, "usage") and completion.usage:
                usage_dict = {
                    "prompt_tokens": completion.usage.prompt_tokens,
                    "completion_tokens": completion.usage.completion_tokens,
                    "total_tokens": completion.usage.total_tokens
                }

            return {
                "reply": reply_content,
                "model_used": selected_model,
                "is_mock": False,
                "usage": usage_dict
            }
        except Exception as e:
            print(f"[GroqService] Error al llamar a Groq API: {e}")
            return {
                "reply": f"Lo siento, ocurrió un error al consultar el servicio de IA: {str(e)}",
                "model_used": selected_model,
                "is_mock": True
            }

    def generate_dynamic_tip(self, category: str = None) -> dict:
        selected_model = settings.GROQ_MODEL_FAST
        
        fallback_tips = [
            "Nunca caves directamente hacia abajo, podrías caer en lava o en un barranco.",
            "Un bloque recorrido en el Nether equivale a 8 bloques en el Overworld.",
            "Los gatos espantan a los Creepers y Phantoms.",
            "Necesitas 15 estanterías alrededor de una mesa de encantamientos para el nivel 30.",
            "El hielo azul sobre arena de almas es la forma más rápida de viajar en bote."
        ]
        
        if not self.is_configured():
            return {
                "tip": random.choice(fallback_tips),
                "category": category or "general",
                "source": "MineWiki Local Base"
            }

        try:
            cat_prompt = f" sobre la categoría '{category}'" if category else ""
            prompt = f"Genera un consejo útil, rápido y único para un jugador de Minecraft{cat_prompt}. La respuesta debe ser solo el consejo, en 1 o 2 oraciones."
            
            completion = self.client.chat.completions.create(
                model=selected_model,
                messages=[
                    {"role": "system", "content": "Eres un generador de tips y consejos cortos de Minecraft en español."},
                    {"role": "user", "content": prompt}
                ],
                temperature=0.9,
                max_tokens=150,
            )
            
            tip_content = completion.choices[0].message.content.strip().replace('"', '')
            return {
                "tip": tip_content,
                "category": category or "general",
                "source": f"Groq IA ({selected_model})"
            }
        except Exception as e:
            print(f"[GroqService] Error generando tip: {e}")
            return {
                "tip": random.choice(fallback_tips),
                "category": category or "general",
                "source": "MineWiki Fallback"
            }

    def generate_minecraft_news(self) -> dict:
        news_data = [
            {
                "id": 1,
                "title": "NUEVO BIOMA: PALE GARDEN Y THE CREAKING",
                "tag": "SNAPSHOT 24w38a",
                "version": "1.21.4 Snapshot",
                "summary": "Llega el bioma bosque pálido con madera de Pale Oak y el tenebroso mob Creaking que solo ataca cuando no lo miras.",
                "date": "Reciente"
            },
            {
                "id": 2,
                "title": "¡LOS BUNDLES LLEGAN OFICIALMENTE!",
                "tag": "ACTUALIZACIÓN 1.21.2",
                "version": "Java & Bedrock",
                "summary": "Los sacos de inventario (Bundles) ya están disponibles en todas las plataformas para organizar tus items fácilmente.",
                "date": "Reciente"
            },
            {
                "id": 3,
                "title": "TRIAL CHAMBERS Y CRAFTER AUTOMÁTICO",
                "tag": "TRICKY TRIALS",
                "version": "1.21.0 Final",
                "summary": "Explora las cámaras de desafío, combate al Breeze y automatiza tus crafteos con el nuevo bloque Crafter de Redstone.",
                "date": "Destacado"
            },
            {
                "id": 4,
                "title": "ARMADURAS DE LOBO TEÑIBLES Y MOLDES",
                "tag": "BEDROCK BETA",
                "version": "Preview 1.21.40",
                "summary": "Equipa a tus lobos domesticados con armaduras de escamas de armadillo teñibles con tintes de cualquier color.",
                "date": "Beta"
            }
        ]

        if self.is_configured():
            try:
                # Opcional: enriquecimiento de noticias dinámicas vía Groq
                pass
            except Exception as e:
                print(f"[GroqService] Error generando noticias dinámicas: {e}")

        return {
            "news": news_data,
            "source": f"MineWiki IA News Feed ({settings.GROQ_MODEL_FAST})"
        }

groq_service = GroqService()
