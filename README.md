# Taskosphere: Unified Corporate & Compliance Suite (Android)

Taskosphere is an all-in-one compliance, accounting, human resources, and workflow management Android application built with Kotlin and Jetpack Compose (Material Design 3).

## Core Modules & Features

1. **Taskosphere Core**:
   - High-performance task management with priorities (Low, Medium, High, Urgent), statuses (Pending, In Progress, Review, Completed), and statutory categories (Taxation, Audit, MCA, Legal, General).
   - Fast interactive todo checklist with instant completion toggles.
   - Real-time task statistics cards.

2. **CompliGenie (Statutory Compliance)**:
   - Calendar tracking for GST (GSTR-1, GSTR-3B), Income Tax (Advance Tax, Form 26Q TDS), and MCA / ROC (AOC-4, DIR-3 KYC).
   - Compliance health score indicator.
   - Recording statutory acknowledgments (ARN, Challan CIN, SRN).

3. **Finix AI (Smart Accounting)**:
   - Revenue, Receivables, and Expense KPI overview.
   - Sales Invoicing with automated 18% GST calculation and payment tracking.
   - Vendor expenses and purchase invoice register.
   - Double-entry Journal Vouchers with ledger accounts and narrations.

4. **People Matrix (HR, Attendance & Payroll)**:
   - Live Attendance Punch Clock with punch-in/out timestamps and active duty status.
   - Attendance history log and employee directory.
   - Leave applications management (Casual, Sick, Earned).
   - Salary Slips breakdown (Basic, HRA, Allowances, PF, TDS, Net Pay).

5. **Records & Passvault Vault**:
   - Corporate client master database with PAN, GSTIN, and contact points.
   - Digital Signature Certificate (DSC) register with physical location tracking and expiration countdowns.
   - Government portal credential repository with masked secret protection.

6. **Trademark Sphere**:
   - Active trademark filings and status tracking (Registered, Objected, Formalities Chk Pass).
   - Interactive Nice Classification (Class 1-45) search directory.

7. **Commercial License Console**:
   - Enterprise multi-tenant license details and active seat monitoring.
   - Real-time diagnostic telemetry and system health checks.

## Architecture

- **Runtime:** Android SDK 36, Kotlin 2.1.0, JDK 21
- **UI Toolkit:** Jetpack Compose with Material 3 Design
- **Architecture Pattern:** MVVM (Model-View-ViewModel) with StateFlow
- **Theme:** Brand navy (`#0D3B66`), emerald green (`#1FAF5A`), and modern M3 color tokens
