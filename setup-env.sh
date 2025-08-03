#!/bin/bash

# Letras Vivas Book API - Environment Setup Script

echo "🚀 Setting up environment for Letras Vivas Book API..."

# Check if .env file already exists
if [ -f ".env" ]; then
    echo "⚠️  .env file already exists. Do you want to overwrite it? (y/N)"
    read -r response
    if [[ ! "$response" =~ ^[Yy]$ ]]; then
        echo "❌ Setup cancelled."
        exit 1
    fi
fi

# Copy template to .env
cp env.template .env

echo "✅ Environment file created successfully!"
echo ""
echo "📝 Please review and edit the .env file with your preferred configuration:"
echo "   nano .env"
echo ""
echo "🔧 Default configuration:"
echo "   - Database: PostgreSQL on localhost:5433"
echo "   - Database Name: letras_db"
echo "   - Username: postgres"
echo "   - Password: postgres"
echo "   - Application Port: 8081"
echo ""
echo "🚀 Next steps:"
echo "   1. Edit .env file if needed"
echo "   2. Run: docker-compose up -d"
echo "   3. Run: ./mvnw spring-boot:run"
echo ""
echo "📖 For more information, see README.md" 