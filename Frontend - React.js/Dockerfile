# Use Node.js image
FROM node:20

# Set working directory
WORKDIR /app

# Copy package files and install dependencies
COPY package.json package-lock.json ./
RUN npm install

# Copy the rest of the app
COPY . .

# Build the app
RUN npm run build

# Serve the app
CMD ["npm", "run", "preview"]

# Expose frontend port
EXPOSE 5173