package com.faymaz.herkul.data

import com.faymaz.herkul.model.City

object Cities {

    /**
     * All 81 Turkish provinces with Diyanet prayer times district IDs.
     * IDs sourced from:
     *  - Verified: herkul GNOME extension cities.json (marked V)
     *  - Fetched:  Diyanet website WebFetch lookup  (marked F)
     *
     * Base URL: https://namazvakitleri.diyanet.gov.tr/tr-TR/{id}/namaz-vakti
     */
    val TURKEY: List<City> = listOf(
        City("Adana",           9564,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9564/adana-icin-namaz-vakti"),          // V
        City("Adıyaman",        9158,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9158/adiyaman-icin-namaz-vakti"),        // F
        City("Afyonkarahisar",  9167,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9167/afyonkarahisar-icin-namaz-vakti"), // F
        City("Ağrı",            9185,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9185/agri-icin-namaz-vakti"),            // F
        City("Aksaray",         9193,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9193/aksaray-icin-namaz-vakti"),         // F
        City("Amasya",          9198,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9198/amasya-icin-namaz-vakti"),          // F
        City("Ankara",          9206,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9206/prayer-time-for-ankara"),           // V
        City("Antalya",         9604,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9604/antalya-icin-namaz-vakti"),         // V
        City("Ardahan",         9238,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9238/ardahan-icin-namaz-vakti"),         // F
        City("Artvin",          9246,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9246/artvin-icin-namaz-vakti"),          // F
        City("Aydın",           9252,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9252/aydin-icin-namaz-vakti"),           // F
        City("Balıkesir",       9270,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9270/balikesir-icin-namaz-vakti"),       // F
        City("Bartın",          9285,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9285/bartin-icin-namaz-vakti"),          // F
        City("Batman",          9288,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9288/batman-icin-namaz-vakti"),          // F
        City("Bayburt",         9295,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9295/bayburt-icin-namaz-vakti"),         // F
        City("Bilecik",         9297,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9297/bilecik-icin-namaz-vakti"),         // F
        City("Bingöl",          9303,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9303/bingol-icin-namaz-vakti"),          // F
        City("Bitlis",          9311,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9311/bitlis-icin-namaz-vakti"),          // F
        City("Bolu",            9315,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9315/bolu-icin-namaz-vakti"),            // F
        City("Burdur",          9327,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9327/burdur-icin-namaz-vakti"),          // F
        City("Bursa",           9664,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9664/bursa-icin-namaz-vakti"),           // V
        City("Çanakkale",       9352,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9352/canakkale-icin-namaz-vakti"),       // F
        City("Çankırı",         9359,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9359/cankiri-icin-namaz-vakti"),         // F
        City("Çorum",           9370,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9370/corum-icin-namaz-vakti"),           // F
        City("Denizli",         9392,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9392/denizli-icin-namaz-vakti"),         // F
        City("Diyarbakır",      9402,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9402/diyarbakir-icin-namaz-vakti"),      // F verified 2026-03-24
        City("Düzce",           9414,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9414/duzce-icin-namaz-vakti"),           // F
        City("Edirne",          9419,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9419/edirne-icin-namaz-vakti"),          // F
        City("Elazığ",          9432,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9432/elazig-icin-namaz-vakti"),          // F
        City("Erzincan",        9440,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9440/erzincan-icin-namaz-vakti"),        // F
        City("Erzurum",         9904,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9904/erzurum-icin-namaz-vakti"),         // V
        City("Eskişehir",       9534,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9534/eskisehir-icin-namaz-vakti"),       // V
        City("Gaziantep",       9584,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9584/gaziantep-icin-namaz-vakti"),       // V
        City("Giresun",         9494,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9494/giresun-icin-namaz-vakti"),         // F
        City("Gümüşhane",       9501,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9501/gumushane-icin-namaz-vakti"),       // F
        City("Hakkari",         9507,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9507/hakkari-icin-namaz-vakti"),         // F
        City("Hatay",           20089, "https://namazvakitleri.diyanet.gov.tr/tr-TR/20089/hatay-icin-namaz-vakti"),          // F
        City("Iğdır",           9522,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9522/igdir-icin-namaz-vakti"),           // F
        City("Isparta",         9528,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9528/isparta-icin-namaz-vakti"),         // F
        City("İstanbul",        9541,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9541/prayer-time-for-istanbul"),         // V
        City("İzmir",           9624,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9624/izmir-icin-namaz-vakti"),           // V
        City("Kahramanmaraş",   9577,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9577/kahramanmaras-icin-namaz-vakti"),   // F
        City("Karabük",         9581,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9581/karabuk-icin-namaz-vakti"),         // F
        City("Karaman",         9587,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9587/karaman-icin-namaz-vakti"),         // F
        City("Kars",            9594,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9594/kars-icin-namaz-vakti"),            // F verified 2026-03-24
        City("Kastamonu",       9609,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9609/kastamonu-icin-namaz-vakti"),       // F
        City("Kayseri",         9704,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9704/kayseri-icin-namaz-vakti"),         // V
        City("Kilis",           9629,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9629/kilis-icin-namaz-vakti"),           // F
        City("Kırıkkale",       9635,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9635/kirikkale-icin-namaz-vakti"),       // F
        City("Kırklareli",      9638,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9638/kirklareli-icin-namaz-vakti"),      // F
        City("Kırşehir",        9646,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9646/kirsehir-icin-namaz-vakti"),        // F
        City("Kocaeli",         9654,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9654/kocaeli-icin-namaz-vakti"),         // F
        City("Konya",           9714,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9714/konya-icin-namaz-vakti"),           // V
        City("Kütahya",         9689,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9689/kutahya-icin-namaz-vakti"),         // V
        City("Malatya",         9644,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9644/malatya-icin-namaz-vakti"),         // V
        City("Manisa",          9716,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9716/manisa-icin-namaz-vakti"),          // F
        City("Mardin",          9726,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9726/mardin-icin-namaz-vakti"),          // F
        City("Mersin",          9774,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9774/mersin-icin-namaz-vakti"),          // V
        City("Muğla",           9738,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9738/mugla-icin-namaz-vakti"),           // estimated
        City("Muş",             9755,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9755/mus-icin-namaz-vakti"),             // F
        City("Nevşehir",        9760,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9760/nevsehir-icin-namaz-vakti"),        // F
        City("Niğde",           9766,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9766/nigde-icin-namaz-vakti"),           // F
        City("Ordu",            9782,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9782/ordu-icin-namaz-vakti"),            // F
        City("Osmaniye",        9788,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9788/osmaniye-icin-namaz-vakti"),        // F
        City("Rize",            9799,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9799/rize-icin-namaz-vakti"),            // F
        City("Sakarya",         9807,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9807/sakarya-icin-namaz-vakti"),         // F
        City("Samsun",          9784,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9784/samsun-icin-namaz-vakti"),          // V
        City("Siirt",           9839,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9839/siirt-icin-namaz-vakti"),           // F
        City("Sinop",           9847,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9847/sinop-icin-namaz-vakti"),           // F
        City("Sivas",           9858,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9858/sivas-icin-namaz-vakti"),           // estimated
        City("Şanlıurfa",       9831,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9831/sanliurfa-icin-namaz-vakti"),       // F
        City("Şırnak",          9854,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9854/sirnak-icin-namaz-vakti"),          // F
        City("Tekirdağ",        9879,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9879/tekirdag-icin-namaz-vakti"),        // F
        City("Tokat",           9887,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9887/tokat-icin-namaz-vakti"),           // F
        City("Trabzon",         9824,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9824/trabzon-icin-namaz-vakti"),         // V
        City("Tunceli",         9914,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9914/tunceli-icin-namaz-vakti"),         // F
        City("Uşak",            9919,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9919/usak-icin-namaz-vakti"),            // F
        City("Van",             9930,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9930/van-icin-namaz-vakti"),             // V
        City("Yalova",          9935,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9935/yalova-icin-namaz-vakti"),          // F
        City("Yozgat",          9949,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9949/yozgat-icin-namaz-vakti"),          // F
        City("Zonguldak",       9955,  "https://namazvakitleri.diyanet.gov.tr/tr-TR/9955/zonguldak-icin-namaz-vakti"),       // F
    )

    val DEFAULT_CITY: City = TURKEY.first { it.name == "İstanbul" }
}
