# Keycloak Setup Guide (Master Realm Admin Access)

This guide explains how to configure a Keycloak client, admin user, and required roles in the **master realm** for application integration.

---

# 1. Create Client in Master Realm

## Client: `master-admin`

### Steps
1. Open Keycloak Admin Console
2. Select **master realm**
3. Go to **Clients → Create client**
4. Configure:
    - **Client ID**: `master-admin`
    - **Client type**: `OpenID Connect`
5. Click **Next**

### Client Configuration
Set the following:

- **Client authentication**: `ON` (confidential client)
- **Service accounts**: `ON` (required for admin API access)
- **Standard flow**: `OFF` (optional, depends on use case)

Save changes.

### Retrieve credentials
- Go to **Credentials tab**
- Copy **Client Secret** (if required)

Use this as the Service Account (secret)

---

# 2. Create Admin User in Master Realm

## User: `admin-master`

### Steps
1. Go to **Users → Create new user**
2. Set:
    - **Username**: `admin-master`
3. Click **Save**

---

## Set Password
1. Open user `admin-master`
2. Go to **Credentials tab**
3. Set:
    - **Password**: (your secure password)
    - **Temporary**: `OFF`
4. Save

---

# 3. Assign Roles to User

The user must have permissions to administer Keycloak and create realms.

### Steps
1. Open user `admin-master`
2. Go to **Role mapping**
3. Click **Assign role**
4. Select **Client roles**
5. Choose client: `realm-management`

---

## Required Roles

Assign:

- `realm-admin` → full administrative access
- `create-realm` → permission to create new realms

---

## Optional Roles (if needed)

- `manage-users`
- `manage-clients`
- `view-realm`

---

# 4. Application Configuration

## application.properties (Quarkus example)

```properties
%dev.quarkus.keycloak.admin-client.username=admin-master
%dev.quarkus.keycloak.admin-client.password=YOUR_PASSWORD