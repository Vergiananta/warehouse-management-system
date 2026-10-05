# Warehouse Management Service 📦

Backend RESTful API untuk mengelola inventaris gudang toko, mencakup manajemen item, varian item (ukuran, warna, tipe), harga, level stok, serta sistem pesanan dengan pencegahan penjualan barang yang habis (*out-of-stock prevention*).

---

## 🚀 Cara Menjalankan dengan Docker Compose

Kamu tidak perlu repot install Java ataupun PostgreSQL di komputer lokal. Semua service (aplikasi Spring Boot dan database PostgreSQL) sudah disiapkan di dalam Docker Compose.

### 1. Prasyarat
Pastikan kamu sudah menginstal:
* [Docker Desktop](https://www.docker.com/products/docker-desktop/) (pastikan Docker sudah dalam keadaan berjalan).

---

### 2. Menjalankan Aplikasi
Buka terminal / PowerShell di folder project ini (`warehouse-service`), lalu jalankan perintah berikut:

```bash
docker compose up -d --build
```

> **Catatan:**
> * `-d`: Menjalankan container di latar belakang (*background*).
> * `--build`: Memastikan Docker membangun image terbaru dari source code aplikasi.
> * Database PostgreSQL akan otomatis menyala terlebih dahulu dan dicek kesehatannya sebelum aplikasi Spring Boot mulai berjalan.

---

### 3. Cek Status & Log

* **Cek status container:**
  ```bash
  docker compose ps
  ```

* **Melihat log aplikasi secara realtime:**
  ```bash
  docker compose logs -f app
  ```

---

### 4. Menghentikan Aplikasi
Jika sudah selesai dan ingin mematikan container:

```bash
docker compose down
```
> Data database PostgreSQL tetap aman tersimpan di volume Docker (`postgres_data`).

---

## 📖 Dokumentasi API & Swagger UI

Setelah container berjalan, kamu bisa langsung melihat seluruh endpoint, mencoba request (*Try it out*), dan melihat contoh request/response melalui Swagger UI di browser:

* **Swagger UI (Interaktif):**  
  👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

* **OpenAPI Spec (JSON):**  
  👉 [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🗄️ Informasi Database (PostgreSQL)

Jika kamu ingin menghubungkan database tool (seperti DBeaver, TablePlus, atau pgAdmin) ke PostgreSQL yang sedang berjalan di Docker:

| Parameter | Nilai |
| :--- | :--- |
| **Host** | `localhost` |
| **Port** | `5432` |
| **Database** | `warehouse-management` |
| **Username** | `postgres` |
| **Password** | `postgrespassword` |

---

## 📌 Ringkasan Endpoint Utama

* **Items (`/api/items`)**:
  * `POST /api/items` - Menambahkan item baru
  * `GET /api/items` - Menampilkan semua item beserta variannya
  * `GET /api/items/{id}` - Menampilkan detail item berdasarkan ID
  * `PUT /api/items/{id}` - Memperbarui data item
  * `DELETE /api/items/{id}` - Menghapus item

* **Item Variants (`/api/items/{itemId}/variants`)**:
  * `POST /api/items/{itemId}/variants` - Menambahkan varian untuk suatu item
  * `GET /api/items/{itemId}/variants` - Menampilkan daftar varian dari suatu item
  * `GET /api/items/{itemId}/variants/{variantId}` - Menampilkan detail varian
  * `PUT /api/items/{itemId}/variants/{variantId}` - Memperbarui data varian
  * `DELETE /api/items/{itemId}/variants/{variantId}` - Menghapus varian

* **Orders & Stock Prevention (`/api/orders`)**:
  * `POST /api/orders` - Membuat pesanan baru (otomatis memvalidasi dan memotong stok, menolak pesanan jika stok habis)
  * `GET /api/orders` - Menampilkan riwayat pesanan
  * `GET /api/orders/{id}` - Menampilkan detail pesanan berdasarkan ID
