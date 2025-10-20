# Review Analyzer with Spring AI

This project is a **Review Analyzer** built with **Java**, **Spring Boot**, and **Thymeleaf**. It uses AI to analyze user reviews, providing insights such as sentiment, severity, and recommended actions. The application supports both free-text reviews and JSON-formatted Google Play reviews.

## Features

- **Analyze Free-Text Reviews**: Submit plain text reviews to analyze sentiment, severity, and recommended actions.
- **Analyze Google Play Reviews**: Submit JSON-formatted Google Play reviews for detailed analysis, including metadata like star rating, app version, and more.
- **Dynamic UI**: Built with **Thymeleaf** and **HTMX** for a responsive and interactive user experience.
- **AI Integration**: Uses AI to process and analyze reviews.

## Technologies Used

- **Java 25**
- **Spring Boot 3.5.6**
- **Thymeleaf** for server-side rendering
- **HTMX** for dynamic UI updates
- **Tailwind CSS** for styling
- **Maven** for dependency management

## Prerequisites

- **Java 25** installed
- **Maven** installed
- An OpenAI API key (if using AI services)

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/username/repo-name.git
   cd repo-name
    ```
2. **Configure the Application**:
Add your OpenAI API key as an Env Var named OPENAI_API_KEY:
```properties
openai.api.key=your-api-key
```
3. **Build the Project**:
   ```bash
   mvn clean install
   ```
4. **Run the Application**:
   ```bash
    mvn spring-boot:run
    ```
5. **Access the Application**:
    Open your browser and navigate to `http://localhost:8080/ui`.
6. **Use the Application**:
   - Submit free-text reviews or JSON-formatted Google Play reviews for analysis.
   - View the analysis results dynamically on the page.
## Example JSON for Google Play Reviews
```json
{
   "text": "Desde la última actualización no puedo iniciar sesión\tLa app crashea al abrir.",
   "originalText": "Since last update, I can't log in. App crashes on open.",
   "lastModified": { "seconds": "1738699900", "nanos": 0 },
   "starRating": 1,
   "reviewerLanguage": "es",
   "device": "flounder",
   "androidOsVersion": 34,
   "appVersionCode": 120030,
   "appVersionName": "12.3.0",
   "thumbsUpCount": 42,
   "thumbsDownCount": 3
}
```
