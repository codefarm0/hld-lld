# 🚗 Parking Lot Management System

A complete implementation of a parking lot management system based on the design document requirements. This Spring Boot application demonstrates various design patterns, concurrency handling, and REST API design.

## 🎯 Features Implemented

### Core Features
- **Vehicle Management**: Support for Cars, Motorcycles, and Trucks
- **Spot Assignment**: Automatic spot finding based on vehicle type with flexible sizing
- **Entry/Exit Flow**: Complete ticket generation and spot management
- **Payment Processing**: Multiple payment methods (Cash, Credit Card)
- **Pricing Strategy**: Flexible hourly pricing with discounts
- **Concurrency Handling**: Thread-safe operations for concurrent entries
- **Real-time Availability**: Live spot availability tracking
- **REST API**: Complete API for integration

### Design Patterns Used
- **Singleton Pattern**: ParkingLot (single instance, global access)
- **Strategy Pattern**: PricingStrategy, PaymentProcessor (flexible behavior)
- **Factory Pattern**: PaymentProcessorFactory (object creation)
- **Template Method**: Vehicle hierarchy (common structure, varied implementation)

### Concurrency Techniques
- Atomic operations (find-and-assign)
- Read-write locks (per spot-type)
- Thread-safe collections (ConcurrentHashMap)
- Synchronized methods (critical sections)

## 🏗️ Architecture

```
src/main/java/com/learn/parking/lot/design/
├── model/                    # Domain models
│   ├── SpotType.java        # Enum for spot types
│   ├── VehicleType.java     # Enum for vehicle types
│   ├── Vehicle.java         # Abstract vehicle class
│   ├── Car.java            # Car implementation
│   ├── Motorcycle.java     # Motorcycle implementation
│   ├── Truck.java          # Truck implementation
│   ├── ParkingSpot.java    # Individual parking spot
│   ├── ParkingTicket.java  # Parking ticket management
│   ├── Floor.java          # Floor with spot management
│   └── Receipt.java        # Exit receipt
├── pricing/                 # Pricing strategy
│   ├── PricingStrategy.java
│   └── HourlyPricingStrategy.java
├── payment/                 # Payment system
│   ├── PaymentMethod.java
│   ├── CashPayment.java
│   ├── CreditCardPayment.java
│   ├── PaymentProcessor.java
│   ├── CashPaymentProcessor.java
│   ├── CreditCardPaymentProcessor.java
│   └── PaymentProcessorFactory.java
├── service/                 # Core business logic
│   └── ParkingLot.java     # Main parking lot service
├── controller/              # REST API controllers
│   ├── ParkingController.java
│   └── DemoController.java
└── ParkingLotDesignApplication.java
```

## 🚀 Getting Started

### Prerequisites
- Java 25+
- Gradle 8+

### Running the Application

1. **Start the application:**
   ```bash
   ./gradlew bootRun
   ```

2. **Access the web interface:**
   Open your browser and go to: `http://localhost:8080`

3. **API Base URL:**
   `http://localhost:8080/api`

## 🎬 Demo Scenarios

### Scenario 1: Basic Car Entry & Exit
```bash
curl http://localhost:8080/api/demo/scenario1
```
Demonstrates a car entering, parking, and exiting with cash payment.

### Scenario 2: Multiple Vehicle Types
```bash
curl http://localhost:8080/api/demo/scenario2
```
Shows different vehicle types (Car, Motorcycle, Truck) entering and some exiting.

### Scenario 3: Concurrent Entries
```bash
curl http://localhost:8080/api/demo/scenario3
```
Tests thread-safe concurrent vehicle entries.

## 🔌 API Endpoints

### Vehicle Entry
```bash
curl -X POST http://localhost:8080/api/parking/entry \
  -H "Content-Type: application/json" \
  -d '{"vehicleType":"CAR","licensePlate":"ABC-1234"}'
```

### Vehicle Exit
```bash
curl -X POST http://localhost:8080/api/parking/exit \
  -H "Content-Type: application/json" \
  -d '{"ticketId":"T1234567890-1","paymentMethod":"CASH","paymentDetails":{"amount":"20.0"}}'
```

### Check Availability
```bash
curl http://localhost:8080/api/parking/availability
```

### Parking Lot Status
```bash
curl http://localhost:8080/api/parking/status
```

## 📊 Parking Lot Configuration

The system is configured with:
- **3 Floors** (F1, F2, F3)
- **Total Spots**: 495 spots
  - Compact spots: 99 (for motorcycles)
  - Regular spots: 348 (for cars)
  - Large spots: 30 (for trucks)
  - Handicapped spots: 18

### Spot Assignment Logic
- Motorcycles can park in: Compact spots
- Cars can park in: Regular spots, Large spots
- Trucks can park in: Large spots only

## 💰 Pricing Model

### Hourly Rates
- Motorcycle: $2.00/hour
- Car: $5.00/hour
- Truck: $10.00/hour

### Discounts
- 20% discount for 24+ hours parking

## 🔒 Concurrency Features

### Thread Safety
- **ReadWriteLock**: Per spot-type locking for optimal performance
- **ConcurrentHashMap**: Thread-safe active tickets storage
- **Synchronized methods**: Critical section protection
- **Atomic operations**: Find-and-assign operations

### Race Condition Prevention
- Atomic spot assignment prevents double-booking
- Fine-grained locking reduces contention
- Thread-safe collections for shared data

## 🧪 Testing

### Manual Testing
1. Use the web interface at `http://localhost:8080`
2. Run demo scenarios via API endpoints
3. Test concurrent operations

### Test Scenarios Covered
- ✅ Basic entry/exit flow
- ✅ Multiple vehicle types
- ✅ Concurrent entries
- ✅ Payment processing
- ✅ Spot availability tracking
- ✅ Error handling

## 🔧 Configuration

### Application Properties
```properties
spring.application.name=parking-lot-design
server.port=8080
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Database
- **H2 In-Memory Database** for demo purposes
- **H2 Console**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:parkinglot`
- **Username**: `sa`
- **Password**: (empty)

## 📈 Performance Considerations

### Optimizations Implemented
- **O(1) spot lookup** by organizing spots by type
- **ReadWriteLock** for concurrent read operations
- **In-memory caching** for active tickets
- **Efficient spot assignment** algorithm

### Scalability Features
- Modular design for horizontal scaling
- Repository pattern for database abstraction
- Strategy pattern for flexible pricing
- Factory pattern for extensible payment methods

## 🚨 Error Handling

### Exception Types
- `RuntimeException`: Invalid operations
- `IllegalArgumentException`: Invalid parameters
- Payment failures and validation errors

### Error Scenarios Covered
- Parking lot full
- Invalid ticket ID
- Payment failures
- Invalid vehicle types
- Invalid payment methods

## 🔮 Future Enhancements

### Potential Extensions
- **Database Persistence**: Replace in-memory with PostgreSQL/MySQL
- **Reservation System**: Pre-book parking spots
- **Real-time Notifications**: WebSocket for live updates
- **Mobile App**: React Native or Flutter app
- **Analytics Dashboard**: Spot utilization metrics
- **Dynamic Pricing**: Surge pricing during peak hours
- **QR Code Tickets**: Generate QR codes for tickets
- **Multi-location Support**: Support multiple parking lots

### Advanced Features
- **Machine Learning**: Predict parking demand
- **IoT Integration**: Sensor-based spot detection
- **Blockchain**: Immutable transaction records
- **Microservices**: Split into separate services

## 📝 Key Learnings

### Design Patterns Applied
1. **Singleton**: Ensures single parking lot instance
2. **Strategy**: Flexible pricing and payment processing
3. **Factory**: Dynamic payment processor creation
4. **Template Method**: Common vehicle behavior structure

### Concurrency Best Practices
1. **Fine-grained locking** for better performance
2. **Atomic operations** for critical sections
3. **Thread-safe collections** for shared data
4. **ReadWriteLock** for read-heavy operations

### API Design Principles
1. **RESTful endpoints** for clear resource access
2. **Consistent response format** with success/error indicators
3. **Proper HTTP status codes** for different scenarios
4. **JSON request/response** for easy integration

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is for educational purposes and demonstrates parking lot system design principles.

---

**Happy Parking! 🚗💨**

