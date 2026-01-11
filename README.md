# NewsBriefService

A Spring Boot application that fetches top news headlines and generates AI-powered summaries using a locally running Ollama instance with the Mistral 7B model.

## Overview

NewsBriefService is a RESTful API that:
1. Fetches the latest top headlines from the [News API](https://newsapi.org/)
2. Sends the articles to a local Ollama instance running Mistral 7B
3. Returns AI-generated summaries in JSON or rendered HTML format

## Technologies

| Technology | Version | Description |
|------------|---------|-------------|
| **Java** | 17 | Programming language |
| **Spring Boot** | 3.2.2 | Application framework |
| **Maven** | 3.x | Build and dependency management |
| **Lombok** | - | Reduces boilerplate code |
| **Caffeine** | 3.1.7 | In-memory caching |
| **Ollama** | Local | Local LLM inference server |
| **Mistral 7B** | mistral:7b | AI model for text summarization |
| **News API** | v2 | External news data source |

## Prerequisites

Before running this application, ensure you have:

1. **Java 17** installed
2. **Maven** installed
3. **Ollama** installed and running locally
4. **Mistral 7B model** pulled in Ollama
5. **News API key** (get one at [newsapi.org](https://newsapi.org/))

### Setting up Ollama

1. Install Ollama from [ollama.ai](https://ollama.ai/)
2. Pull the Mistral 7B model:
   ```bash
   ollama pull mistral:7b
   ```
3. Ensure Ollama is running (default: `http://localhost:11434`)

## Configuration

The application is configured via `src/main/resources/application.yaml`:

```yaml
spring:
  application:
    name: newBriefService

news.api:
  api-key: YOUR_NEWS_API_KEY
  base-url: https://newsapi.org/v2
  top-headlines-endpoint: /top-headlines
  default-country: us

ollama:
  base-url: http://localhost:11434/api/generate
  mistral-model: mistral:7b
```

**Important:** Replace `YOUR_NEWS_API_KEY` with your actual News API key.

## Building the Application

```bash
mvn clean install
```

## Running the Application

```bash
mvn spring-boot:run
```

Or run the JAR directly:

```bash
java -jar target/newBriefService-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`.

## API Endpoints

### Get News Summary (JSON)

```
GET /api/v1/news-brief/general-brief
```

Returns a JSON response with the AI-generated news summary.

**Response:**
```json
{
  "summary": "Today's top news includes...",
  "createdAt": "2026-01-11T21:30:00"
}
```

### Get News Summary (HTML)

```
GET /api/v1/news-brief/general-brief/render
```

Returns a fully rendered HTML page with the news summary, styled with inline CSS. This endpoint is designed to be viewed directly in a browser.

## Project Structure

```
src/main/java/com/jehan/newsBriefService/
├── NewsBriefServiceApplication.java    # Main application entry point
├── client/
│   ├── NewsApiClient.java              # Client for fetching news from News API
│   └── OllamaClient.java               # Client for interacting with Ollama
├── config/
│   └── CacheConfig.java                # Caffeine cache configuration
├── controller/
│   └── NewsBriefController.java        # REST API endpoints
├── dto/
│   ├── Article.java                    # News article DTO
│   ├── NewsApiResponse.java            # News API response DTO
│   ├── NewsSummaryResponse.java        # Summary response DTO
│   ├── OllamaRequest.java              # Ollama request DTO
│   └── OllamaResponse.java             # Ollama response DTO
└── service/
    └── NewsBriefService.java           # Business logic for generating briefs
```

## How It Works

1. **Fetch Headlines**: The `NewsApiClient` calls the News API to retrieve the top headlines for the configured country.

2. **Generate Summary**: The `OllamaClient` sends the article titles and content to the local Ollama instance with a prompt instructing Mistral 7B to summarize the news.

3. **Return Response**: The `NewsBriefService` orchestrates the flow and returns either:
   - A JSON response with the summary text
   - A rendered HTML page for browser viewing

4. **Caching**: Results are cached using Caffeine to reduce API calls and improve response times.

## License

See [LICENSE](LICENSE) for details.
