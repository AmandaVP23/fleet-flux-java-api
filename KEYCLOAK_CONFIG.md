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

### Add service account roles
1. Go to **Service account roles**
2. Assign realm role **admin**

### Retrieve credentials
- Go to **Credentials tab**
- Copy **Client Secret** (if required)

Use this as the Service Account (secret)

---

# 2. Application Configuration

## application.properties (Quarkus example)

```properties
%dev.quarkus.keycloak.admin-client.username=admin-master
%dev.quarkus.keycloak.admin-client.password=YOUR_PASSWORD