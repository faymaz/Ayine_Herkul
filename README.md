# Herkul — Namaz Vakitleri & Radyo

Diyanet İşleri Başkanlığı'nın resmi sitesinden namaz vakitlerini çeken, İslami radyo istasyonlarını yayınlayan Android uygulaması. **Ayine.tv** cihazları için özel olarak tasarlanmıştır.

![Herkul Ana Ekran](screenshots/screenshot_home.png)

---

## Özellikler

### Namaz Vakitleri
- Türkiye'nin **81 ili** için Diyanet'ten canlı veri çekme
- Günlük ve aylık görünüm
- Sonraki namaza geri sayım sayacı
- **Hicri + Miladi** tarih gösterimi
- Anlık saat

### Radyo
| İstasyon | Açıklama |
|---|---|
| Herkul Radyo | İslami içerik ve müzik |
| Cihan Radyo | Müzik ve kültür |
| Sadece Müzik | Türk ve dünya müziği |

### Arayüz
- Tam ekran (immersive) mod — status bar ve navigation bar gizlenir
- Ekran her zaman açık kalır (ekran koruyucuya düşmez)
- Kuran ayetleri, hadisler ve İslami sözler rotasyonu
- Koyu tema

---

## Gereksinimler

| Özellik | Değer |
|---|---|
| Minimum Android | 6.0 Marshmallow (API 23) |
| Hedef Android | 11 (API 30) |
| Mimari | armeabi-v7a |
| İnternet | Gerekli (Diyanet + radyo akışı) |

---

## Kurulum

### APK ile (Ayine cihazı)

```bash
adb install -r app-debug.apk
```

### Kaynak koddan derle

```bash
git clone https://github.com/faymaz/herkul.git
cd herkul
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## Proje Yapısı

```
app/src/main/java/com/faymaz/herkul/
├── MainActivity.kt              # Ana activity, fullscreen yönetimi
├── fragment/
│   ├── HomeFragment.kt          # Ana ekran (saat, hadis, namaz barı)
│   ├── PrayerTimesFragment.kt   # Günlük/aylık vakit tablosu
│   └── RadioFragment.kt         # Radyo akışı (ExoPlayer)
├── data/
│   ├── Cities.kt                # 81 il Diyanet ID'leri
│   ├── RadioStations.kt         # Radyo istasyon listesi
│   ├── Hadiths.kt               # Hadis koleksiyonu
│   └── Quotes.kt                # İslami sözler
└── util/
    ├── PrayerTimeFetcher.kt     # Diyanet web scraping (Jsoup)
    └── PrayerTimeCache.kt       # Günlük veri önbelleği
```

---

## Teknoloji

- **Kotlin** — Android geliştirme dili
- **ExoPlayer** — radyo akışı
- **Jsoup** — Diyanet HTML scraping
- **Coroutines** — asenkron işlemler
- **ViewBinding** — layout bağlama
- **Material Components** — alt navigasyon çubuğu

---

## İlham Kaynağı

Bu uygulama, GNOME masaüstü için geliştirilmiş [herkul](https://github.com/faymaz/herkul) GNOME Shell eklentisinden ilham alınarak Android'e taşınmıştır.

---

## Ayine.tv

Bu uygulama, **Ayine.tv** Android cihazları için optimize edilmiştir.

[![Ayine.tv](https://img.shields.io/badge/Ayine.tv-Cihaz%20Uyumlu-green)](https://www.ayine.tv)

> [www.ayine.tv](https://www.ayine.tv) — İslami içerik yayın cihazları
