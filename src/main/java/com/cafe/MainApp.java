package com.cafe;

import com.cafe.auth.AuthService;
import com.cafe.auth.Session;
import com.cafe.model.*;
import com.cafe.system.CaffeSystem;

import java.util.Optional;

/**
 * Main entry point.
 * Simulasi flow interaktif dengan CLI.
 */
public class MainApp {

    private static AuthService auth = new AuthService();
    private static CaffeSystem caffe = new CaffeSystem();
    private static Optional<Session> currentSession = Optional.empty();
    
    // PERBAIKAN: Deklarasikan currentOrder sebagai field statis (variabel kelas)
    private static Order currentOrder = null;

    public static void main(String[] args) {
        UIUtils.printHeader("SELAMAT DATANG DI CAFE ORDER SYSTEM");
        mainMenu();
    }

    private static void mainMenu() {
        while (currentSession.isEmpty()) {
            UIUtils.printHeader("MAIN MENU");
            System.out.println("1. Login");
            System.out.println("2. Register Customer Baru");
            System.out.println("3. Keluar");

            int choice = UIUtils.readMenuChoice(3);

            try {
                switch (choice) {
                    case 1:
                        handleLogin();
                        break;
                    case 2:
                        handleRegister();
                        break;
                    case 3:
                        UIUtils.printMessage("Terima kasih, sampai jumpa!");
                        return;
                }
            } catch (RuntimeException e) {
                UIUtils.printError(e.getMessage());
            }
        }

        if (currentSession.isPresent()) {
            Role role = currentSession.get().getUser().getRole();
            if (role == Role.CUSTOMER) {
                customerMenu();
            } else if (role == Role.BARISTA) {
                baristaMenu();
            } else {
                UIUtils.printError("Role " + role + " belum didukung.");
                handleLogout();
            }
        }
    }

    private static void handleLogin() {
        UIUtils.printHeader("LOGIN");
        String email = UIUtils.readString("Masukkan Email");
        String password = UIUtils.readString("Masukkan Password");

        Session session = auth.login(email, password);
        currentSession = Optional.of(session);
        UIUtils.printMessage("Login berhasil! Selamat datang, " + session.getUser().getNama() + " (" + session.getUser().getRole() + ")");
    }

    private static void handleRegister() {
        UIUtils.printHeader("REGISTER CUSTOMER BARU");
        String nama = UIUtils.readString("Nama");
        String email = UIUtils.readString("Email");
        String password = UIUtils.readString("Password");

        User u = auth.register(nama, email, password);
        UIUtils.printMessage("Register berhasil! Silakan login dengan email: " + u.getEmail());
    }

    private static void handleLogout() {
        currentSession = Optional.empty();
        // Reset currentOrder saat logout
        currentOrder = null; 
        UIUtils.printMessage("Anda telah logout.");
        mainMenu();
    }

    // --- CUSTOMER FLOW ---
    private static void customerMenu() {
        User customer = currentSession.get().getUser();
        
        // Periksa apakah Order baru perlu dibuat (jika belum ada atau sudah lunas)
        if (currentOrder == null || currentOrder.getStatus() == OrderStatus.PAID) {
            currentOrder = caffe.buatOrder(customer);
            UIUtils.printMessage("Memulai order baru.");
        }

        while (true) {
            UIUtils.printHeader("CUSTOMER MENU - " + customer.getNama());
            System.out.println("Order Status: " + currentOrder.getStatus());
            System.out.println("1. Lihat Menu");
            System.out.println("2. Tambah Item ke Order");
            System.out.println("3. Lihat Keranjang & Total (Total: " + currentOrder.total() + ")");
            System.out.println("4. Kirim Pesanan (SUBMIT)");
            System.out.println("5. Bayar Pesanan (Jika sudah READY)");
            System.out.println("6. Logout");

            int choice = UIUtils.readMenuChoice(6);

            try {
                switch (choice) {
                    case 1:
                        lihatMenu();
                        break;
                    case 2:
                        tambahItemToOrder(currentOrder);
                        break;
                    case 3:
                        lihatKeranjang(currentOrder);
                        break;
                    case 4:
                        kirimPesanan(currentOrder);
                        break;
                    case 5:
                        handlePembayaran(currentOrder);
                        // handlePembayaran akan membuat order baru atau tetap di loop jika pembayaran gagal
                        break;
                    case 6:
                        handleLogout();
                        return;
                }
            } catch (Exception e) {
                UIUtils.printError(e.getMessage());
            }
        }
    }

    private static void lihatMenu() {
        UIUtils.printHeader("DAFTAR MENU CAFE");
        caffe.lihatMenu().forEach(m ->
                System.out.printf("  - %-15s (Rp %,.0f)\n", m.getNama(), m.getHarga())
        );
    }

    private static void tambahItemToOrder(Order order) {
        // ... (kode tetap sama)
        UIUtils.printHeader("TAMBAH ITEM");
        lihatMenu();
        String namaMenu = UIUtils.readString("Masukkan Nama Menu yang ingin dipesan");
        int qty = UIUtils.readInt("Jumlah (Qty)");

        if (qty <= 0) {
            UIUtils.printError("Kuantitas harus lebih dari 0.");
            return;
        }

        boolean success = caffe.tambahItem(order, namaMenu, qty);
        if (success) {
            UIUtils.printMessage(qty + "x " + namaMenu + " berhasil ditambahkan ke keranjang.");
        } else {
            UIUtils.printError("Menu '" + namaMenu + "' tidak ditemukan.");
        }
    }

    private static void lihatKeranjang(Order order) {
        // ... (kode tetap sama)
        UIUtils.printHeader("KERANJANG PESANAN - STATUS: " + order.getStatus());
        if (order.getItems().isEmpty()) {
            UIUtils.printMessage("Keranjang Anda kosong.");
            return;
        }

        for (OrderItem item : order.getItems()) {
            System.out.printf("  - %d x %s (Rp %,.0f) = Rp %,.0f\n",
                    item.getQty(), item.getMenu().getNama(), item.getMenu().getHarga(), item.subtotal());
        }
        System.out.printf("\nTOTAL PESANAN: Rp %,.0f\n", order.total());
    }

    private static void kirimPesanan(Order order) {
        // ... (kode tetap sama)
        if (order.getStatus() != OrderStatus.DRAFT) {
            UIUtils.printError("Pesanan sudah dikirim sebelumnya (Status: " + order.getStatus() + ").");
            return;
        }
        caffe.kirimPesanan(order);
        UIUtils.printMessage("Pesanan Anda telah dikirim ke Barista. Silakan tunggu.");
    }

    private static void handlePembayaran(Order order) {
        if (order.getStatus() != OrderStatus.READY) {
            UIUtils.printError("Pesanan belum siap untuk dibayar (Status: " + order.getStatus() + ").");
            return;
        }

        UIUtils.printHeader("PEMBAYARAN");
        double total = order.total();
        System.out.printf("Total yang harus dibayar: Rp %,.0f\n", total);
        
        while(true) {
            double bayar = UIUtils.readInt("Masukkan jumlah uang bayar");
            if (bayar < total) {
                UIUtils.printError("Uang yang dimasukkan kurang.");
            } else {
                caffe.bayar(order);
                double kembalian = bayar - total;
                System.out.printf("Kembalian: Rp %,.0f\n", kembalian);
                UIUtils.printMessage("Pembayaran berhasil! Order selesai.");
                
                System.out.println(order.cetakStruk().getText());
                
                // PERBAIKAN: Mengupdate field statis 'currentOrder' dengan Order baru
                currentOrder = caffe.buatOrder(order.getPemesan());
                UIUtils.printMessage("Order baru telah dibuat secara otomatis.");
                return;
            }
        }
    }

    // --- BARISTA FLOW ---
    private static void baristaMenu() {
        UIUtils.printHeader("BARISTA MENU - " + currentSession.get().getUser().getNama());
        UIUtils.printMessage("Saat ini, sistem belum memiliki list Order yang sedang diproses. Anggap Barista berinteraksi dengan Order terakhir yang dibuat oleh Customer.");
        UIUtils.printMessage("Untuk simulasi, silakan jalankan sesi Customer terlebih dahulu untuk membuat dan SUBMIT Order.");

        System.out.println("1. Logout");
        int choice = UIUtils.readMenuChoice(1);

        if (choice == 1) {
            handleLogout();
        }
    }
}