# Thesis and Capstone Defense Schedule Management Application

Android Kotlin app with Jetpack Compose UI and Ktor Client, plus a PHP REST API for XAMPP Apache/MySQL.

## Android Setup

1. Open this folder in Android Studio.
2. Let Gradle sync.
3. The app uses Jetpack Compose, Material 3, Ktor Client, Gson, SharedPreferences, and simple MVVM packages:
   - `data/api`
   - `data/model`
   - `data/repository`
   - `ui/screens`
   - `ui/theme`
   - `viewmodel`

## XAMPP Database

1. Start XAMPP.
2. Start Apache and MySQL.
3. Open `http://localhost/phpmyadmin`.
4. Go to Import.
5. Select `database.sql`.
6. Click Go.

The database name is `thesis_schedule_db`.

## PHP Backend Placement

1. Create this folder:
   `C:\xampp\htdocs\thesis_schedule_api`
2. Copy all files from the project `backend` folder into that XAMPP folder.
3. Test one endpoint in a browser:
   `http://localhost/thesis_schedule_api/get_professors.php`

The PHP connection is in `backend/db.php`. Default XAMPP credentials are used:

```php
$username = "root";
$password = "";
```

## Android API Base URL

The base URL is in:

`app/src/main/java/com/example/thesisschedulemanagementapp/data/api/ApiClient.kt`

For Android Emulator:

```kotlin
const val EMULATOR_BASE_URL = "http://10.0.2.2/thesis_schedule_api/"
```

For a physical device, change `baseUrl` to your computer LAN IP, for example:

```kotlin
var baseUrl: String = "http://192.168.1.10/thesis_schedule_api/"
```

Your phone and computer must be connected to the same Wi-Fi network.

## Sample Accounts

Seed password for all accounts: `password`

Student:

```text
student@example.com
```

Professor adviser:

```text
adviser@example.com
```

Professor panelists:

```text
panelist@example.com
panelist2@example.com
```

You can also create new student and professor accounts from the Sign Up screen. PHP stores new passwords using `password_hash()` and login verifies them using `password_verify()`.

## Testing Flow

1. Import `database.sql`.
2. Copy `backend` files to `C:\xampp\htdocs\thesis_schedule_api`.
3. Start Apache and MySQL in XAMPP.
4. Run the Android app on an emulator.
5. Login as `adviser@example.com` with password `password`.
6. Open Create Schedule.
7. Select the seeded group, room, date, start time, end time, and panelists.
8. Submit the schedule.
9. Login as `student@example.com` to view the assigned schedule.
10. Login as `panelist@example.com` to view schedules where that professor is assigned as panelist.

Advisers can also edit, cancel, complete, or permanently delete schedules from the professor schedule list. Delete uses `backend/delete_schedule.php` and requires the logged-in professor to be the schedule adviser.

Conflict checks are handled by the backend before saving:

- room already booked
- adviser has another schedule
- panelist has another schedule
- student group already has a schedule

The API returns JSON like:

```json
{
  "success": false,
  "message": "Room is already booked for the selected date and time."
}
```

The Android app shows the message in a Snackbar.
