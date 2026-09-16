# vivx ⚡

> **A modern, declarative form validation, automated focus chaining, and UI state toolkit for Jetpack Compose.**

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.20-7F52FF.svg?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.09.00-4285F4.svg?logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-21-3DDC84.svg?logo=android&logoColor=white)](https://developer.android.com)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

---

## 🎯 The Problem in Standard Compose

Building real-world forms in Jetpack Compose is notoriously repetitive and verbose:
- ❌ **Focus Requester Hell**: Moving the cursor to the next input when pressing the keyboard's **"Next"** button requires creating and wiring separate `FocusRequester` objects for every single field.
- ❌ **Dirty vs Touched States**: Showing error messages immediately annoys users. Writing custom logic to show errors only on blur, change, or submit takes 40+ lines of boilerplate.
- ❌ **Inter-Field Validation**: Validating that `confirmPassword` matches `password` requires manual state hoisting and synchronization.
- ❌ **First-Error Auto Focus**: When a user clicks "Submit", finding and focusing the first invalid input takes manual inspection.

**`vivx` solves all of this in under 20 lines of clean, idiomatic Kotlin code.**

---

## ✨ 60-Second Quick Start

```kotlin
@Composable
fun LoginForm(onLoginSuccess: (email: String) -> Unit) {
    // 1. Declare schema with validation rules
    val form = rememberVivxForm {
        field("email") {
            required("Email is required")
            email("Please enter a valid email")
        }
        field("password") {
            required("Password is required")
            minLength(6, "Password must be at least 6 characters")
        }
    }

    // 2. Wrap in VivxForm container
    VivxForm(formState = form) {
        // 3. Inputs automatically chain focus (Next -> Next -> Done)
        VivxTextField(
            key = "email",
            label = "Email Address",
            keyboardType = KeyboardType.Email
        )

        verticalSpacer(12.dp)

        // 4. Built-in password toggle
        VivxPasswordField(
            key = "password",
            label = "Password"
        )

        verticalSpacer(24.dp)

        // 5. Submit button validates all fields & auto-focuses first error
        VivxSubmitButton(label = "Sign In") { data ->
            onLoginSuccess(data["email"]!!)
        }
    }
}
```

---

## 🚀 Key Features

### 1. 🔗 Automated Focus Progression
Never write `val focusRequester = remember { FocusRequester() }` again.
- Fields in a `VivxForm` automatically receive `ImeAction.Next`.
- Pressing **Next** on the software keyboard automatically transfers focus to the subsequent input.
- The final field automatically switches to `ImeAction.Done` and dismisses the keyboard.

### 2. 🛡️ Comprehensive Validation DSL
Built-in rules include:
```kotlin
val form = rememberVivxForm {
    field("username") {
        required("Username cannot be empty")
        minLength(3, "At least 3 characters")
        maxLength(20, "At most 20 characters")
    }
    field("email") {
        required()
        email()
    }
    field("phone") {
        regex(Regex("^[0-9]{10}$"), "Enter a valid 10-digit number")
    }
    field("confirmPassword") {
        required("Please re-enter your password")
        matches("password", "Passwords must match") // Inter-field matching!
    }
    field("age") {
        custom("You must be at least 18") { value ->
            (value.toIntOrNull() ?: 0) >= 18
        }
    }
}
```

### 3. 👁️ Password Field with Instant Toggle
`VivxPasswordField` comes with built-in show/hide visibility icons, `PasswordVisualTransformation`, and focus progression out-of-the-box.

### 4. 🔄 Server Error Injection
Handle backend validation conflicts smoothly:
```kotlin
// Inject API validation errors directly onto the field
form.setFieldError("email", "This email is already registered")
```

### 5. 📦 LCE (Loading, Content, Error, Empty) State Machine
Handle screen states declaratively with animated crossfades:
```kotlin
VivxContent(
    state = uiState, // VivxLce.Loading, Content, Error, or Empty
    onRetry = { viewModel.reload() }
) { data ->
    DashboardView(data)
}
```

### 6. 📐 Scope-Aware Spacers
Avoid Compose square-constraint bugs where `Modifier.size()` distorts layouts:
```kotlin
Column {
    verticalSpacer(16.dp) // Only sets height
    verticalSpacer(1f)     // Weighted vertical spacer
}

Row {
    horizontalSpacer(12.dp) // Only sets width
    horizontalSpacer(1f)   // Weighted horizontal spacer
}
```

### 7. 🎬 Media Overlay Scaffold (`VivxOverlayScaffold`)
Specifically designed for media apps (like video/audio players):
- Anchors MiniPlayer above system navigation bar.
- Floats navigation pill above MiniPlayer with auto-hide animation on search / multiselect.
- Automatically calculates dynamic bottom content padding so lists never get cut off or hidden behind floating controls!

```kotlin
VivxOverlayScaffold(
    isMiniPlayerVisible = isPlaying,
    miniPlayer = { MiniPlayerBar(...) },
    floatingBottomBar = { FloatingNavBar(...) }
) { contentPadding ->
    LazyColumn(contentPadding = contentPadding) {
        items(mediaList) { ... }
    }
}
```

### 8. 💬 Modal Form Dialogs & BottomSheets
Launch forms inside modal dialogs or bottom sheets with zero boilerplate:
```kotlin
// Modal Form Dialog (e.g. Open Stream URL)
VivxFormDialog(
    form = streamForm,
    title = "Open Network Stream",
    onConfirm = { data -> player.play(data["streamUrl"]!!) }
) {
    VivxTextField(key = "streamUrl", label = "Stream URL")
}

// Modal Form BottomSheet (e.g. Playback Speed & Settings)
VivxBottomSheetForm(
    form = settingsForm,
    title = "Playback Settings",
    onSave = { data -> ... }
) {
    VivxNumberField(key = "speed", label = "Speed", step = 0.25, min = 0.5, max = 2.0, isDecimal = true, suffix = "x")
    VivxSwitchField(key = "hwAccel", title = "Hardware Acceleration")
}
```

### 9. 💬 Chat & Cloud Media UI (`io.github.vivx.ui.chat`)
Specifically crafted for modern messenger & media cloud apps:
- **`VivxMessageBubble`**: Outgoing/Incoming message bubbles with reply quotes, timestamps, and double-check read receipts (`✓✓`).
- **`VivxChatListItem`**: Conversation list item with verified badge, unread pill counters, and online indicators.
- **`VivxDocumentBubble`**: Media / Video file attachment with circular progress download/play button.
- **`VivxPinnedBar`**: Pinned message strip anchored at the top of chat/channel feeds.

```kotlin
// Incoming message with reply quote
VivxMessageBubble(
    text = "Check out this movie in Telegram Cloud!",
    time = "11:32 AM",
    isOutgoing = false,
    replyAuthor = "Cloud Bot",
    replySnippet = "Dune_Part_Two.mkv ready"
)

// Document attachment with live circular download button
VivxDocumentBubble(
    fileName = "Movie_1080p.mkv",
    fileSize = "2.4 GB",
    isOutgoing = false,
    state = VivxAttachmentState.READY,
    onActionClick = { player.play() }
)
```

---

## 📁 Repository Structure

```
vivx/
├── vivx/                         # Core Library Module
│   └── src/main/kotlin/io/github/vivx/
│       ├── VivxDsl.kt            # DSL marker annotation
│       ├── form/                 # Form engine, state & validation rules
│       ├── components/           # VivxTextField, VivxPasswordField, VivxNumberField, VivxSwitchField, VivxDropdownField
│       ├── dialog/               # VivxFormDialog, VivxBottomSheetForm
│       ├── settings/             # VivxSettings DSL (Category, SwitchPref, SliderPref, ActionPref)
│       ├── state/                # VivxContent LCE state machine
│       ├── layout/               # VivxOverlayScaffold & scope-aware spacers
│       └── ui/
│           ├── chat/             # VivxMessageBubble, VivxChatListItem, VivxDocumentBubble, VivxPinnedBar, VivxBadge
│           ├── glass/            # VivxGlass, VivxGlassCard, VivxGlassPill
│           ├── ambient/          # VivxAmbientGlow
│           ├── player/           # VivxPlayerControls, VivxPlayerSlider
│           ├── shimmer/          # VivxShimmer, VivxShimmerBox
│           └── animation/        # VivxTactileClick, VivxExpandableText
│   └── src/test/                 # Comprehensive unit tests
└── sample/                       # Showcase Demo App
    └── src/main/kotlin/io/github/vivx/sample/
        ├── MainActivity.kt       # 6-Tab showcase container
        └── screens/              # Login, Register, LCE, Media Demo, Player UI & Chat UI screens
```

---

## 📥 Installation

### Option 1: Direct Maven Dependency
Add the dependency to your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.vivx:vivx:1.0.0")
}
```

### Option 2: Local Composite Build (Instant integration into existing apps)
To use `vivx` directly in your local project (e.g., `NextPlayer`) without waiting for Maven Central:
In your project's `settings.gradle.kts`:

```kotlin
includeBuild("../vivx")
```
And in your module's `build.gradle.kts`:
```kotlin
dependencies {
    implementation("io.github.vivx:vivx")
}
```

---

## 🧪 Running Tests

To run the unit test suite:
```bash
./gradlew test
```

---

## 📄 License

```
Copyright 2026 Vivx Contributors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
```
