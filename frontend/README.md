# CraveBite Frontend

Modern food ordering client built with **React**, **Vite**, and **Tailwind CSS**.

## Getting Started

### 1. Start the Spring Boot Backend
From the root directory:
```bash
./mvnw spring-boot:run
```
Backend will start on `http://localhost:8080`.

### 2. Start the React Frontend Dev Server
From the `frontend` directory:
```bash
npm run dev
```
Frontend will be available at `http://localhost:5173`.

## Features
- **Restaurant Explorer**: Browse restaurants, search by name/city, view operating statuses.
- **Menu & Dish Ordering**: View dishes with price, stock status, and add to cart.
- **Cart & Checkout**: Slide-out drawer with quantity controls, delivery address input, notes, and instant checkout.
- **Order Tracking**: Live status badges (`PENDING`, `PREPARING`, `OUT_FOR_DELIVERY`, `DELIVERED`, `CANCELLED`) with cancellation support.
- **Authentication**: JWT token management for sign-in and sign-up.
- **Restaurant Owner Portal**: Kitchen dashboard for restaurant owners to accept and advance orders through preparation and delivery.

