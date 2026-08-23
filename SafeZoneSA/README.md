# SafeZone SA — Android Application

> Community Safety Reporting Platform | Rosebank College (IIE)

---

## Project Structure

```
SafeZoneSA/
├── app/src/main/
│   ├── AndroidManifest.xml
│   ├── java/com/example/safezonesa/
│   │   ├── models/        Incident.kt
│   │   ├── network/       ApiService.kt, RetrofitClient.kt
│   │   └── ui/            SplashActivity, LoginActivity, RegisterActivity,
│   │                      DashboardActivity, SubmitIncidentActivity, MyIncidentsActivity
│   └── res/
│       ├── layout/        All screen XML layouts
│       ├── drawable/      Custom backgrounds & icons
│       └── values/        colors.xml, strings.xml, themes.xml
└── backend/               PHP API + SQL setup (for XAMPP)
```

---

## How to Open in Android Studio

1. Open **Android Studio** (Hedgehog or newer recommended)
2. Click **File → Open** and select the `SafeZoneSA` folder
3. Wait for Gradle sync to complete (first time may take 2–5 minutes)
4. Once synced, press **Run ▶** or `Shift+F10` to launch on emulator

---

## Backend Setup (XAMPP — Optional for full functionality)

1. Start **XAMPP** and ensure **Apache** and **MySQL** are running
2. Open **phpMyAdmin** → run `backend/setup_database.sql`
3. Copy the `backend/` folder into `C:/xampp/htdocs/safezone_api/`
4. The app's `RetrofitClient` already points to `http://10.0.2.2/safezone_api/`

> Without XAMPP running, the app still works — submissions fall back to "offline mode" with a success toast.

---

## Tech Stack

| Layer    | Technology              |
|----------|-------------------------|
| Mobile   | Kotlin, Android SDK 35  |
| Network  | Retrofit2 + Gson        |
| Backend  | PHP 8 + PDO             |
| Database | MySQL (via XAMPP)       |
| Design   | Material Design 3, Dark Theme |

---

## Screens

| Screen           | File                        | Description                        |
|------------------|-----------------------------|------------------------------------|
| Splash           | SplashActivity.kt           | Animated logo, auto-navigates      |
| Login            | LoginActivity.kt            | Email + password validation        |
| Register         | RegisterActivity.kt         | 4-field registration form          |
| Dashboard        | DashboardActivity.kt        | Stats, alerts, quick actions       |
| Report Incident  | SubmitIncidentActivity.kt   | Spinner, description, API POST     |
| My Reports       | MyIncidentsActivity.kt      | RecyclerView list with badges      |

---

## Student Info

- **Student Number:** ST10440743
- **Institution:** Rosebank College (IIE)
- **Module:** Mobile Application Development
