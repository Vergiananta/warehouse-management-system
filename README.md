# Warehouse Management Service

Backend RESTful API untuk mengelola inventaris gudang toko. Sistem ini mencakup pencatatan data item, varian item (seperti perbedaan warna atau spesifikasi), pengaturan harga dan stok, serta pencatatan pesanan yang dilengkapi pencegahan penjualan saat stok habis (out-of-stock prevention).

---

## Cara Menjalankan Aplikasi

Aplikasi ini dan database PostgreSQL sudah dikemas menggunakan Docker Compose, sehingga kamu tidak perlu menginstal Java ataupun PostgreSQL secara manual di komputer.

### Prasyarat
Pastikan aplikasi Docker Desktop sudah terinstal dan sedang berjalan di komputer kamu.

### Langkah Menjalankan
1. Buka terminal atau PowerShell di dalam folder project ini (`warehouse-service`).
2. Jalankan perintah berikut:
   ```bash
   docker compose up -d --build
   ```
   Perintah ini akan mengunduh image database, membangun image aplikasi Spring Boot, dan menyalakan keduanya di latar belakang.

3. Untuk melihat log jalannya aplikasi, jalankan:
   ```bash
   docker compose logs -f app
   ```
   Tunggu beberapa detik sampai muncul keterangan bahwa aplikasi Spring Boot sudah aktif di port 8080.

4. Jika ingin menghentikan aplikasi:
   ```bash
   docker compose down
   ```
   Data database kamu akan tetap tersimpan aman di volume Docker.

---

## Halaman Dokumentasi API (Swagger UI)

Setelah aplikasi berjalan, kamu bisa melihat dan mencoba langsung seluruh endpoint melalui browser:

* Swagger UI Interaktif: http://localhost:8080/swagger-ui.html
* Spesifikasi OpenAPI JSON: http://localhost:8080/v3/api-docs

---

## Panduan Alur Penggunaan API (Langkah demi Langkah)

Agar memudahkan kamu dalam mencoba API dari awal hingga akhir, ikuti urutan pemanggilan endpoint berikut melalui Swagger UI:

### Langkah 1: Buat Item Baru
Sebelum menambahkan varian atau pesanan, buat item dasar terlebih dahulu.

* Method: POST
* URL: `/api/items`
* Contoh Request Body:
  ```json
  {
    "sku": "ITEM-001",
    "name": "Wireless Mechanical Keyboard",
    "description": "Keyboard mechanical wireless 75% dengan RGB",
    "price": 350000.00,
    "stockQuantity": 50
  }
  ```
* Catat nilai `id` yang didapat dari respons (misalnya `id: 1`) untuk digunakan pada langkah berikutnya.

---

### Langkah 2: Tambahkan Varian pada Item
Item yang sudah dibuat bisa memiliki beberapa varian (misalnya jenis switch keyboard atau warna).

* Method: POST
* URL: `/api/items/1/variants` (ganti angka 1 dengan `id` item yang dibuat pada Langkah 1)
* Contoh Request Body:
  ```json
  {
    "sku": "ITEM-001-RED",
    "name": "Red Linear Switch",
    "price": 365000.00,
    "stockQuantity": 20
  }
  ```
* Catat nilai `id` varian yang dihasilkan (misalnya `variantId: 1`).

---

### Langkah 3: Periksa Daftar Item dan Stoknya
Periksa apakah item dan varian yang kamu buat sudah tersimpan dengan benar di dalam sistem.

* Method: GET
* URL: `/api/items` atau `/api/items/1`
* Respons akan menampilkan data item beserta seluruh varian yang dimilikinya dan sisa stok masing-masing.

---

### Langkah 4: Buat Pesanan (Order)
Sekarang kamu bisa mencoba membuat transaksi pemesanan. Sistem akan secara otomatis mengurangi jumlah stok barang yang dipesan.

* Method: POST
* URL: `/api/orders`
* Contoh Request Body:
  ```json
  {
    "items": [
      {
        "itemId": 1,
        "variantId": 1,
        "quantity": 2
      }
    ]
  }
  ```
* Pesanan berhasil dibuat dengan status `COMPLETED`. Stok varian `Red Linear Switch` yang awalnya berjumlah 20 akan otomatis berkurang menjadi 18.

---

### Langkah 5: Uji Pencegahan Penjualan Barang Habis (Out-of-Stock Prevention)
Untuk membuktikan bahwa sistem menolak pesanan jika stok tidak mencukupi, coba lakukan order dengan jumlah melebihi stok yang tersisa.

* Method: POST
* URL: `/api/orders`
* Contoh Request Body (meminta 50 unit, padahal stok varian hanya tersisa 18 unit):
  ```json
  {
    "items": [
      {
        "itemId": 1,
        "variantId": 1,
        "quantity": 50
      }
    ]
  }
  ```
* Sistem akan menolak pesanan dan mengembalikan pesan error `400 Bad Request` yang menjelaskan bahwa stok tidak mencukupi. Stok barang di gudang pun tidak akan terpotong karena transaksi otomatis dibatalkan secara aman.

---

### Langkah 6: Cek Riwayat Pesanan
Untuk melihat daftar seluruh pesanan yang pernah dibuat:

* Method: GET
* URL: `/api/orders` atau `/api/orders/{id}`

---

### Langkah 7: Perbarui Data Item atau Varian (Update)
Jika ingin mengubah informasi barang seperti nama, harga, atau menambah stok gudang:

* Update Item:
  * Method: PUT
  * URL: `/api/items/1`
* Update Varian:
  * Method: PUT
  * URL: `/api/items/1/variants/1`

---

### Langkah 8: Hapus Item (Delete)
Jika suatu produk sudah tidak dijual lagi dan ingin dihapus dari gudang:

* Method: DELETE
* URL: `/api/items/1`
* Menghapus item induk akan otomatis menghapus seluruh data varian yang berada di bawahnya.

---

## Informasi Koneksi Database

Jika kamu ingin memeriksa isi tabel secara langsung menggunakan aplikasi database seperti DBeaver atau pgAdmin:

* Host: localhost
* Port: 5432
* Database: warehouse-management
* Username: postgres
* Password: postgrespassword
