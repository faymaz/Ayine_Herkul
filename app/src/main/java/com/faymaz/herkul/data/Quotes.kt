package com.faymaz.herkul.data

data class Quote(val text: String, val author: String)

/** Unified display item shown on the home screen. */
sealed class DisplayItem {
    data class QuoteItem(val quote: Quote) : DisplayItem()
    data class HadithItem(val hadith: Hadith) : DisplayItem()
}

object DisplayItems {
    val ALL: List<DisplayItem> by lazy {
        val items = mutableListOf<DisplayItem>()
        Quotes.ALL.forEach { items.add(DisplayItem.QuoteItem(it)) }
        Hadiths.ALL.forEach { items.add(DisplayItem.HadithItem(it)) }
        items.shuffle()
        items
    }
}

object Quotes {
    val ALL = listOf(
        Quote("Gönül Çalab'ın tahtı, Çalab gönüle baktı,\nİki cihan bedbahtı, kim gönül yıkar ise.", "Yunus Emre"),
        Quote("İlim ilim bilmektir,\nİlim kendin bilmektir,\nSen kendini bilmezsen,\nBu nice okumaktır.", "Yunus Emre"),
        Quote("Sevelim sevilelim,\nBu dünya kimseye kalmaz.", "Yunus Emre"),
        Quote("Biz dünyaya sultan olmaya gelmedik,\nBiz âşık olmaya, sevmeye geldik.", "Yunus Emre"),
        Quote("Kolaylık gösterin, zorlaştırmayın.\nMüjdeleyin, nefret ettirmeyin.", "Hz. Muhammed (s.a.v.)"),
        Quote("Güçlü olan güreşte başkasını yenen değil,\nöfkelenince kendine hâkim olandır.", "Hz. Muhammed (s.a.v.)"),
        Quote("Komşusu açken tok yatan bizden değildir.", "Hz. Muhammed (s.a.v.)"),
        Quote("En hayırlınız, ahlakça en güzel olanınızdır.", "Hz. Muhammed (s.a.v.)"),
        Quote("Güzel söz de bir sadakadır.", "Hz. Muhammed (s.a.v.)"),
        Quote("Cennet annelerin ayakları altındadır.", "Hz. Muhammed (s.a.v.)"),
        Quote("Kim neyi severse onunla beraberdir.", "Hz. Muhammed (s.a.v.)"),
        Quote("Mümin, kendisi için istediğini\nkardeşi için de istemedikçe gerçek mümin olamaz.", "Hz. Muhammed (s.a.v.)"),
        Quote("İnsanlara teşekkür etmeyen,\nAllah'a da şükretmiş olmaz.", "Hz. Muhammed (s.a.v.)"),
        Quote("Zorlukla birlikte kolaylık vardır.\nŞüphesiz zorlukla birlikte kolaylık vardır.", "İnşirah Suresi, 5-6"),
        Quote("Allah sabredenlerle beraberdir.", "Bakara Suresi, 153"),
        Quote("Biz insanı en güzel biçimde yarattık.", "Tin Suresi, 4"),
        Quote("Şüphesiz Allah, güzel davrananları sever.", "Bakara Suresi, 195"),
        Quote("De ki: Ey kullarım! Kendinize karşı aşırı gidip günahlar işleyenler,\nAllah'ın rahmetinden umut kesmeyin.", "Zümer Suresi, 53"),
        Quote("Dünya tatlı ve caziptir.\nAllah sizi orada halife kılmış ve nasıl davranacağınızı görmektedir.", "Hz. Muhammed (s.a.v.)"),
        Quote("Her doğan çocuk, fıtrat üzere doğar.", "Hz. Muhammed (s.a.v.)"),
        Quote("Ey gönül, sen bu dünyada bir yolcusun,\nBu yolculukta sen hep ağlayacaksın.", "Hz. Mevlana"),
        Quote("Ney gibi her dem nefes çekip inle,\nO ateşle yan ki dünya yansin.", "Hz. Mevlana"),
        Quote("Söz az ol, mana çok;\nDeniz gibi derin, gökyüzü gibi saf ol.", "Hz. Mevlana"),
        Quote("Canlar canını buldum,\nBu can ile cananı.", "Hz. Mevlana"),
        Quote("İnsanı yaşat ki devlet yaşasın.", "Hacı Bektaş Veli"),
        Quote("Eline, diline, beline sahip ol.", "Hacı Bektaş Veli"),
        Quote("Bir olalım, diri olalım, iri olalım.", "Hacı Bektaş Veli"),
        Quote("Dünya gamına dalmayın,\nO gam insanı soldurur.", "Hz. Ali (r.a.)"),
        Quote("İlim öğrenmek her Müslüman'a farzdır.", "Hz. Muhammed (s.a.v.)"),
        Quote("Beşikten mezara kadar ilim öğreniniz.", "Hz. Muhammed (s.a.v.)")
    )
}
