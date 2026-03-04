FROM maven:3.9.4-eclipse-temurin-17

WORKDIR /app

# Copy Maven files first for layer caching
COPY pom.xml .

# Download dependencies separately (cached layer)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Run tests at container runtime
CMD ["mvn", "clean", "test"]