from typing import List, Dict
import math
from app.config import settings
from app.services.groq_service import groq_service

# ============================================================
# BASE DE CONOCIMIENTO EXPANDIDA — MineWiki RAG Store
# 25+ documentos de Minecraft organizados por categorías
# Fuente de datos INTERNA del sistema MineWiki
# ============================================================
MINECRAFT_KNOWLEDGE_BASE = [
    # --- DIMENSIONES ---
    {
        "category": "dimensiones",
        "title": "Viaje por el Nether y Portales",
        "content": "La proporción de distancia entre el Nether y el Overworld es de 1:8. Viajar 1 bloque en el Nether equivale a avanzar 8 bloques en el Overworld. Para construir un portal del Nether se requieren mínimo 10 bloques de obsidiana (marco 4x5 sin esquinas) y activarlo con pedernal y hierro. La obsidiana requiere pico de DIAMANTE o NETHERITA para recolectarse (el pico de hierro no funciona)."
    },
    {
        "category": "dimensiones",
        "title": "El End y el Dragon del End",
        "content": "El End es la dimensión final accesible a través de la Fortaleza del End. Para activar el portal se necesitan Ojos de Ender en los marcos. El Dragon del End tiene 200 puntos de vida y se cura con Cristales del End. Al derrotarlo suelta 12.000 puntos de experiencia, un Huevo de Dragon y abre el portal al End interior. La Estrella del Nether NO la suelta el Dragon, la suelta el Wither."
    },
    {
        "category": "dimensiones",
        "title": "Biomas del Nether",
        "content": "El Nether contiene 5 biomas: Nether Wastes (base con Ghasts y Zombie Piglins), Crimson Forest (hongos rojos, Hoglins y Piglins), Warped Forest (el más seguro, solo Enderman), Soul Sand Valley (arena de almas, Ghasts, Wither Skeletons) y Basalt Deltas (Magma Cubes). La Fortaleza del Nether contiene Blaze Spawners y cofres con recursos valiosos."
    },
    # --- CRAFTEO ---
    {
        "category": "crafteo",
        "title": "Faro (Beacon) Receta y Activacion",
        "content": "Receta del Faro (Grid 3x3): Fila 1=[Cristal,Cristal,Cristal], Fila 2=[Cristal,Estrella del Nether,Cristal], Fila 3=[Obsidiana,Obsidiana,Obsidiana]. La Estrella del Nether la suelta el WITHER (no el Dragon del End). El Cristal se obtiene fundiendo arena en el horno. La obsidiana requiere pico de diamante. Para activar el Faro se necesita una piramide de bloques de mineral: Nivel 1=9 bloques (3x3), Nivel 2=34 bloques, Nivel 3=83 bloques, Nivel 4=164 bloques (9x9). Proporciona efectos permanentes de Velocidad, Fuerza, Prisa Minera o Regeneracion."
    },
    {
        "category": "crafteo",
        "title": "Mesa de Encantamientos y Librerias",
        "content": "Receta de la Mesa de Encantamientos (Grid 3x3): Fila 1=[vacio,Libro,vacio], Fila 2=[Diamante,Obsidiana,Diamante], Fila 3=[Obsidiana,Obsidiana,Obsidiana]. Para alcanzar encantamientos de Nivel 30 (maximo) se necesitan exactamente 15 estanterias colocadas a 1 bloque de distancia alrededor de la mesa, con el espacio intermedio libre de bloques."
    },
    {
        "category": "crafteo",
        "title": "Herramientas de Netherita",
        "content": "Para crear herramientas de Netherita: obtén Netherita Antigua (Ancient Debris) en el Nether entre Y=8 y Y=22. Fundela en el horno para obtener Fragmentos de Netherita. Combina 4 fragmentos + 4 lingotes de oro = 1 lingote de Netherita. Actualiza herramientas de diamante con la Mesa de Herreria (Smithing Table) + Molde de Netherita. La Netherita es más resistente que el diamante, flota en lava y no arde."
    },
    {
        "category": "crafteo",
        "title": "Pociones y Elaboracion (Brewing Stand)",
        "content": "Para elaborar pociones se necesita un Soporte para Pociones (1 vara de Blaze + 3 piedras). Proceso: llenar botellas de vidrio con agua, añadir Verruga del Nether para hacer Pociones Raras (base), luego añadir ingredientes: Crema de Magma=Resistencia al Fuego, Zanahoria Dorada=Vision Nocturna, Ojo de Arana Fermentado=invertir efectos. La Polvora convierte pociones en arrojadizas."
    },
    {
        "category": "crafteo",
        "title": "Brujula y Mapa del Mundo",
        "content": "Receta de Brujula: 4 lingotes de hierro alrededor de 1 polvo de redstone en el centro. Apunta siempre al Punto de Aparicion original. Para crear un mapa: 8 papeles alrededor de una brujula. El mapa puede ampliarse colocandolo en el centro rodeado de 8 papeles adicionales hasta nivel 4. Para duplicar: coloca el mapa junto a uno en blanco en la mesa de crafteo."
    },
    # --- MOBS ---
    {
        "category": "mobs",
        "title": "El Warden y la Ciudad Antigua Deep Dark",
        "content": "El Warden aparece en las Ciudades Antiguas (Ancient Cities) en el bioma Deep Dark. Se invoca al activar 3 veces los Sculk Shriekers. Tiene 500 puntos de vida (el mob mas fuerte del juego). Es ciego y detecta jugadores por vibraciones y olor. El encantamiento Swift Sneak (Sigilo Rapido) en pantalones reduce vibraciones al moverse agachado. Se recomienda evitarlo, no combatirlo."
    },
    {
        "category": "mobs",
        "title": "El Wither Invocacion y Recompensa",
        "content": "El Wither es invocado con 4 bloques de Arena de Almas en forma de T + 3 Calaveras de Wither Skeleton encima. Tiene 300 puntos de vida en Java Edition. Al morir suelta la Estrella del Nether (necesaria para el Faro/Beacon). Estrategia: invocar bajo tierra para evitar daños al mundo. Usar armadura de diamante o netherita con encantamiento Proteccion."
    },
    {
        "category": "mobs",
        "title": "Piglins y Trueque Nether",
        "content": "Los Piglins son neutrales en el Nether pero atacan si el jugador no lleva al menos una pieza de armadura de oro. El trueque se hace tirando lingotes de oro al suelo. Los Piglins se zombifican si entran al Overworld. Los Hoglins son hostiles y se pueden criar con Hongos Carmesi; huyen de las Verrugas del Nether."
    },
    {
        "category": "mobs",
        "title": "Creepers Phantoms y Gatos",
        "content": "Los gatos y ocelotes asustan a Creepers y Phantoms haciendolos huir. Los Phantoms aparecen si no se ha dormido por mas de 3 dias en el juego. Para evitar explosiones de Creeper usar un escudo que bloquea el 100% del daño. Si un Esqueleto mata a un Creeper este suelta un Disco de Musica. Los Creepers son inmunes al fuego."
    },
    {
        "category": "mobs",
        "title": "Aldeanos y Comercio Profesiones",
        "content": "Los Aldeanos tienen profesiones segun el bloque de trabajo: Granjero=Composta, Bibliotecario=Atril, Herrero=Forja de Piedra, Cartografo=Mesa de Cartografia. Para curar Aldeano Zombie: 1) Pocion Arrojadiza de Debilidad, 2) Manzana Dorada. Tras curarse ofrece descuentos permanentes. Los oficios se pueden resetear si el aldeano no ha hecho tratos aun."
    },
    {
        "category": "mobs",
        "title": "El Creaking Mob del Pale Garden",
        "content": "El Creaking es un mob hostil de Minecraft 1.21.4 vinculado al bioma Pale Garden. Esta conectado al bloque Corazon Creaking: mientras este intacto el Creaking es inmortal. Solo se mueve cuando el jugador NO lo mira directamente. Si el jugador lo mira fijamente el Creaking se paraliza. Para derrotarlo se debe destruir el bloque Corazon Creaking."
    },
    # --- ENCANTAMIENTOS ---
    {
        "category": "encantamientos",
        "title": "Guia Completa de Encantamientos",
        "content": "Encantamientos clave: Fortuna III (multiplica drops de diamantes, esmeraldas, carbon, lapislazuli, NO funciona en minerales de hierro u oro crudos). Toque de Seda (Silk Touch): recolecta el bloque original. Reparacion (Mending): repara con orbes de XP. Irrompibilidad III: triplica durabilidad. Eficiencia V: maxima velocidad de minado. Proteccion IV: reduce cualquier daño. Filo V (Sharpness V): mayor daño en espadas. Poder V (Power V): mayor daño en arcos."
    },
    {
        "category": "encantamientos",
        "title": "Combinacion de Encantamientos en Yunque",
        "content": "El Yunque permite combinar herramientas y aplicar libros encantados. Cada uso incrementa el castigo del yunque (aumenta costo en XP). Encantamientos incompatibles que no se pueden combinar: Fortuna con Toque de Seda. Proteccion con Proteccion contra Explosiones, Fuego o Proyectiles. Filo con Daño a No-Muertos o Artropodos."
    },
    # --- REDSTONE ---
    {
        "category": "redstone",
        "title": "Mecanicas de Redstone Avanzadas",
        "content": "La senal de Redstone se transmite hasta 15 bloques. El Repetidor extiende senales y anlade retardos de 1-4 ticks (0.1-0.4 segundos). El Comparador compara o resta senales y lee contenedores. Los Pistones normales y pegajosos mueven hasta 12 bloques en cadena. Un tick de Redstone = 0.1 segundos. Los circuitos NOT AND OR se construyen con antorchas y repetidores."
    },
    {
        "category": "redstone",
        "title": "Granja de Hierro Automatica",
        "content": "Una granja de hierro explota la generacion natural de Golems de Hierro. Se necesitan: 10 aldeanos, 10 camas, y 75% dormidos recientemente. El Golem aparece cada 35 segundos si se cumplen las condiciones. Para automatizar: plataformas con aldeanos sobre lava o agua para que el Golem caiga y muera soltando 3-5 lingotes de hierro y amapolas."
    },
    # --- BIOMAS ---
    {
        "category": "biomas",
        "title": "Biomas Nuevos de Minecraft 1.18 a 1.21",
        "content": "Biomas añadidos recientemente: Lush Caves 1.18 con Axolotes y musgo. Dripstone Caves 1.18 con estalactitas. Deep Dark 1.19 con Warden y Ciudad Antigua. Mangrove Swamp 1.19 con arboles de manglar y ranas. Cherry Grove 1.20 con cerezos rosas. Pale Garden 1.21.4 con madera Pale Oak y el Creaking."
    },
    {
        "category": "biomas",
        "title": "Generacion de Minerales en 1.18 en adelante",
        "content": "Desde la version 1.18 la generacion de minerales cambio: Diamantes mayor frecuencia en Y=-58 rango Y=-64 a Y=16. Hierro picos en Y=16 y Y=232 en montanas. Cobre mas frecuente en Y=48. Lapislazuli mas frecuente en Y=0. Carbon mas frecuente en Y=96. Netherita Antigua en el Nether mejor ratio en Y=15 rango Y=8 a Y=119."
    },
    {
        "category": "biomas",
        "title": "Trial Chambers y el Breeze Minecraft 1.21",
        "content": "Las Trial Chambers Camaras de Prueba se anadieron en Minecraft 1.21. Generan entre Y=-40 y Y=40. Contienen Trial Spawners que generan mobs en oleadas y sueltan Trial Keys. Los Ominous Trial Spawners son mas dificiles y sueltan Ominous Trial Keys. El Breeze lanza rafagas de viento. Los cofres Vault requieren Trial Keys y pueden contener la nueva Maza (Mace)."
    },
    # --- SUPERVIVENCIA ---
    {
        "category": "supervivencia",
        "title": "Gestion de Alimentos y Hambre",
        "content": "La barra de hambre tiene 20 puntos. Con hambre mayor a 18 la salud se regenera. Con hambre menor o igual a 6 no se puede correr. Con hambre en 0 se pierde salud. Alimentos mas eficientes: Zanahoria Dorada 14.4 saturacion la mejor, Chuleta de Cerdo Cocinada y Bistec 12.8. La Torta restaura 14 puntos pero debe colocarse en el mundo. Siempre cocinar carne antes de comer."
    },
    {
        "category": "supervivencia",
        "title": "Estrategias de Minado Eficiente",
        "content": "Para minar diamantes ir a Y=-58 y usar minado en franjas tuneles de 1x2 con ramas cada 3 bloques. Usar Fortuna III para multiplicar diamantes hasta 4 por bloque. Pico con Eficiencia V mas Prisa Minera maximiza velocidad. Siempre llevar cubos de agua para enfriar lava. Nunca cavar hacia arriba sin verificar puede caer lava o arena."
    },
    {
        "category": "supervivencia",
        "title": "Curacion de Aldeanos Zombie",
        "content": "Para curar a un Aldeano Zombie: 1) Lanzar Pocion Arrojadiza de Debilidad al zombie. 2) Darle una Manzana Dorada con clic derecho. Tras unos minutos se cura y ofrece descuentos permanentes en sus tratos. Es la manera mas eficiente de obtener encantamientos baratos de Librero como Reparacion o Toque de Seda."
    },
    {
        "category": "supervivencia",
        "title": "Elytras y Vuelo Avanzado",
        "content": "Las Elytras se encuentran en los Barcos del End en las ciudades del End exterior tras derrotar al Dragon del End. Para repararlas: Membranas de Fantasma en un Yunque o encantamiento Mending. Para volar: saltar desde altura y presionar saltar en el aire para planear. Los Cohetes de Fuegos Artificiales aceleran el vuelo. Las Elytras se equipan en el slot de pecho reemplazando la armadura de pecho."
    }
]


class RagService:
    def __init__(self):
        self.docs = MINECRAFT_KNOWLEDGE_BASE
        categorias = set(d['category'] for d in self.docs)
        print(f"[RagService] Base de conocimiento cargada: {len(self.docs)} documentos en {len(categorias)} categorias: {categorias}")

    def _tfidf_similarity(self, query: str, text: str) -> float:
        """Scoring TF-IDF simplificado para busqueda de relevancia."""
        query_words = query.lower().split()
        text_lower = text.lower()
        text_words = text_lower.split()
        if not query_words or not text_words:
            return 0.0
        score = 0.0
        total_text_words = len(text_words)
        for qw in query_words:
            tf = text_lower.count(qw) / max(total_text_words, 1)
            title_bonus = 3.0 if qw in text_lower[:100] else 1.0
            score += tf * title_bonus
        return score / math.sqrt(len(query_words))

    def search_knowledge(self, query: str, top_k: int = 3) -> List[Dict]:
        """Recupera los top_k documentos mas relevantes para la query dada."""
        scored_docs = []
        for doc in self.docs:
            combined_text = f"{doc['title']} {doc['title']} {doc['content']}"
            score = self._tfidf_similarity(query, combined_text)
            scored_docs.append({
                "title": doc["title"],
                "content": doc["content"],
                "category": doc.get("category", "general"),
                "score": float(score)
            })
        scored_docs.sort(key=lambda x: x["score"], reverse=True)
        top_results = scored_docs[:top_k]

        # Trazabilidad de datos — IE4 coherencia datos/respuestas
        print(f"[RagService] Query: '{query}' → Top-{top_k} recuperados:")
        for i, doc in enumerate(top_results):
            print(f"  [{i+1}] '{doc['title']}' | cat={doc['category']} | score={doc['score']:.4f}")

        return top_results

    def ask_rag(self, question: str, top_k: int = 3) -> dict:
        """
        Pipeline RAG completo:
        1. Retrieval: busca documentos relevantes en la base de conocimiento interna
        2. Augmentation: construye un prompt enriquecido con el contexto recuperado
        3. Generation: genera la respuesta final con el LLM de Groq
        """
        # PASO 1 — RETRIEVAL
        retrieved_docs = self.search_knowledge(question, top_k=top_k)

        # PASO 2 — AUGMENTATION
        context_str = "\n\n".join([
            f"[Doc {i+1}: {d['title']} | Categoria: {d['category']} | Relevancia: {d['score']:.3f}]\n{d['content']}"
            for i, d in enumerate(retrieved_docs)
        ])

        # PASO 3 — GENERATION
        if groq_service.is_configured():
            rag_prompt = (
                f"Eres MineWiki Assistant. Responde la pregunta usando EXCLUSIVAMENTE "
                f"la informacion del contexto de la wiki proporcionado abajo. "
                f"Si la respuesta no esta en el contexto, indicalo claramente.\n\n"
                f"=== CONTEXTO DE LA WIKI ===\n{context_str}\n\n"
                f"=== PREGUNTA DEL USUARIO ===\n{question}\n\n"
                f"Responde en espanol con datos precisos basados solo en el contexto dado."
            )
            chat_res = groq_service.generate_chat_reply(message=rag_prompt, use_fast_model=True)
            return {
                "answer": chat_res["reply"],
                "sources": retrieved_docs,
                "model_used": chat_res["model_used"],
                "docs_retrieved": len(retrieved_docs)
            }
        else:
            top_source = retrieved_docs[0] if retrieved_docs else None
            ans = (
                f"Segun MineWiki ({top_source['title'] if top_source else 'Wiki'}): "
                f"{top_source['content'] if top_source else 'No se encontro informacion relacionada.'}"
            )
            return {
                "answer": ans,
                "sources": retrieved_docs,
                "model_used": "local-rag-store",
                "docs_retrieved": len(retrieved_docs)
            }


rag_service = RagService()
