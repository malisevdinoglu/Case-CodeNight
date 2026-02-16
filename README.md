# Case 1

<!-- Buraya Case 1.docx dosyaCodenight Case: Turkcell Game+ Quest League
Görev, Puan, Rozet ve Leaderboard Sistemi
Amaç
Game+ kullanıcılarının oyun içi aktivitelerinden “görev” tamamlamalarını hesaplayan, puan veren, rozet kazandıran ve liderlik tablosu (leaderboard) üreten bir sistem geliştirmeniz beklenmektedir.
Sistem aşağıdaki işleri yapmalıdır:
1.	Aktivite verilerini okumalıdır.
2.	Kullanıcı bazlı metrikleri (bugün, son 7 gün, streak vb.) hesaplamalıdır.
3.	Görevleri veri tabanlı (CSV’den) çalıştırmalıdır.
4.	Aynı gün birden fazla görev tetiklenirse tek bir ödül seçmelidir (çakışma kuralı).
5.	Puan defteri (ledger) mantığı ile puan hareketlerini kaydetmelidir.
6.	Toplam puanlara göre leaderboard üretmelidir.
7.	Rozet (badge) koşullarını kontrol edip rozetleri atamalıdır.
8.	Bildirim (mock) üretmelidir.
9.	Basit bir dashboard ile sonuçları gösterebilmelidir.
 
1. Veri Kaynakları
1.1 Kullanıcılar
users.csv ile gelir.
1.2 Oyunlar
games.csv ile gelir.
1.3 Aktivite Olayları (Event)
activity_events.csv ile gelir. Her satır bir kullanıcının bir gün içindeki özet aktivitesidir.
Alanlar:
•	user_id
•	date
•	game_id
•	login_count
•	play_minutes
•	pvp_wins
•	coop_minutes
•	topup_try
Not: Bu case’te event’ler günlük özet olduğu için “stream” zorunlu değil; veriyi okuyup hesaplamalar yapılması yeterlidir.
 
2. Türetilen Metrikler (State)
Belirli bir tarih için (as_of_date) aşağıdaki metrikleri üretmeniz gerekir:
Bugün (as_of_date):
•	login_count_today
•	play_minutes_today
•	pvp_wins_today
•	coop_minutes_today
•	topup_try_today
Son 7 gün (as_of_date dahil):
•	play_minutes_7d
•	topup_try_7d
•	logins_7d
Streak:
•	login_streak_days
As_of_date’den geriye doğru, ardışık günlerde login_count >= 1 ise kaç gün devam ettiğini hesaplayın.
Bu metrikleri bir user_state çıktısı olarak tutmanız beklenir.
 
3. Görev (Quest) Motoru
Görevler quests.csv içindedir ve veri bazlı çalıştırılmalıdır.
Her görev:
•	quest_id
•	quest_name
•	quest_type (DAILY, WEEKLY, STREAK)
•	condition (metinsel)
•	reward_points
•	priority
•	is_active
Sistem:
1.	is_active = true görevleri değerlendirir.
2.	Her kullanıcı için hangi görevlerin sağlandığını bulur (triggered_quests).
Görev örnekleri (seed data’da var):
•	Günlük Giriş: login_count_today >= 1
•	Kesintisiz Seri: login_streak_days >= 3
•	PvP Ustası: pvp_wins_today >= 3
•	Coop Takım Oyunu: coop_minutes_today >= 60
•	Haftalık Maraton: play_minutes_7d >= 600
•	Harcamaya Ödül: topup_try_7d >= 200
 
4. Çakışma Yönetimi (Tek Ödül Kuralı)
Bir kullanıcı için aynı gün birden fazla görev tetiklenebilir.
Bu durumda:
•	priority değeri en küçük olan görev seçilir (1 en yüksek öncelik).
•	Diğer tetiklenen görevler suppressed olarak kaydedilir.
•	Kullanıcıya o gün için sadece seçilen görevin puanı eklenir.
Çıktı olarak quest_awards üretmeniz beklenir:
•	award_id
•	user_id
•	as_of_date
•	triggered_quests
•	selected_quest
•	reward_points
•	suppressed_quests
•	timestamp
 
5. Puan Defteri (Points Ledger)
Puan güncellemelerini doğrudan “users.total_points” gibi bir alana yazmak yerine, bir puan hareket kaydı (ledger) tutmanız beklenir.
points_ledger örnek alanları:
•	ledger_id
•	user_id
•	points_delta
•	source (QUEST_REWARD)
•	source_ref (award_id)
•	created_at
Toplam puan, ledger’dan türetilmelidir (sum(points_delta)).
 
6. Leaderboard Üretimi
As_of_date için leaderboard üretin:
•	rank
•	user_id
•	total_points
Sıralama:
•	total_points yüksekten düşüğe
•	eşitlikte user_id alfabetik
leaderboard.csv olarak kaydedin veya üretin.
 
7. Rozet (Badge) Sistemi
Rozetler badges.csv içinde eşiklerle tanımlıdır.
Örnek:
•	total_points >= 300 -> Bronz
•	total_points >= 800 -> Gümüş
•	total_points >= 1500 -> Altın
Sistem:
1.	total_points hesaplar.
2.	Koşulu sağlayan rozetleri kullanıcıya atar.
3.	badge_awards çıktısı üretir:
o	user_id
o	badge_id
o	awarded_at
 
8. Bildirim (Mock)
Görev ödülü kazanıldığında kullanıcıya bir bildirim üretin (mock).
Örnek alanlar:
•	notification_id
•	user_id
•	channel (BiP)
•	message
•	sent_at
 
9. Dashboard (Zorunlu)
Web tabanlı bir ekran yeterlidir. Mobil zorunlu değildir.
Ekranda en az şu bölümler olmalıdır:
•	Kullanıcı listesi ve total_points
•	Leaderboard (ilk 10)
•	Kullanıcı detayında: bugün metrikleri, son 7 gün metrikleri, streak
•	Triggered quests / selected quest / suppressed quests
•	Kazanılan rozetler
•	Bildirim kayıtları
 
Bonus (Opsiyonel)
1.	Quest yönetim ekranı (ekle/güncelle/aktif-pasif/priority değiştir).
2.	What-if simülasyonu: “Bugün +2 PvP win olsaydı hangi görev seçilirdi?”
3.	Gün gün puan grafiği (ledger üzerinden).
 
Puanlama (Genel – 100)
•	Temel İşlevsellik ve Doğru Çalışma: 30
•	Veri Modeli ve Sistem Tasarımı: 20
•	Kural ve Karar Mekanizması: 20
•	Kod Kalitesi ve Yapı: 15
•	Görsellik ve Anlatılabilirlik: 10
•	Bonus Özellikler: 5
sının içeriğini yapıştırabilirsiniz -->

