package com.example.appcuriosity

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private var userId: String = ""

    private lateinit var myUserId: String
    var topicsList: MutableList<String> = mutableListOf()

    private var scienza :Boolean = false
    private var natura :Boolean = false
    private var storia :Boolean = false
    private var arte :Boolean = false
    private var corpo :Boolean = false
    private var viaggi :Boolean = false
    private var cibo :Boolean = false


    /* base output
    * Toast.makeText(this, "tutto OK", Toast.LENGTH_SHORT).show()
    * */

    private companion object{
        private const val CHANNEL_ID = "default"
        private const val NOTIFICATION_ID = 1
        private val NOTIFICATION_DELAY = 5000L  // 5 secondi di ritardo
        private var button1Clicked = false
        private var button2Clicked = false
    }

    val curiositaScienzaTecnologia = listOf(
        "Il genoma umano è costituito da circa 3 miliardi di coppie di basi di DNA.",
        "La luce viaggia a circa 299,792,458 metri al secondo, la velocità massima possibile secondo la teoria della relatività di Einstein.",
        "Il teletrasporto quantistico consente di trasferire lo stato quantistico di una particella da un punto a un altro senza che la particella viaggi fisicamente tra i due punti.",
        "Internet è stato sviluppato negli anni '60 come progetto di ricerca avanzata negli Stati Uniti, ed è cresciuto fino a diventare una rete globale che connette miliardi di dispositivi e persone in tutto il mondo.",
        "I computer quantistici sfruttano i principi della meccanica quantistica per eseguire calcoli molto più velocemente rispetto ai computer classici.",
        "Nella meccanica quantistica, il principio di sovrapposizione quantistica afferma che un sistema fisico può esistere in più stati simultaneamente fino a quando non viene osservato o misurato.",
        "Gli esseri umani hanno messo piede sulla Luna per la prima volta durante la missione Apollo 11 nel 1969.",
        "La nanotecnologia manipola la materia a livello atomico e molecolare, consentendo la creazione di materiali e dispositivi con proprietà avanzate.",
        "Il primo programma di intelligenza artificiale fu scritto nel 1956 da Allen Newell e Herbert A. Simon.",
        "La teoria del Big Bang suggerisce che l'Universo si sia formato circa 13,8 miliardi di anni fa da una singolarità infinitamente densa e calda.",
        "Il primo smartphone, il Simon Personal Communicator, è stato lanciato nel 1992 da IBM e offriva funzionalità di telefonia, fax, e-mail e calendario.",
        "Il sistema operativo Android è stato acquisito da Google nel 2005 e è diventato il sistema operativo mobile più diffuso al mondo, alimentando miliardi di dispositivi.",
        "Il primo computer commerciale, l'UNIVAC I, è stato consegnato nel 1951 e pesava circa 13 tonnellate, con un costo di circa 1,2 milioni di dollari.",
        "Il World Wide Web è stato inventato nel 1989 da Tim Berners-Lee come un sistema per consentire la condivisione di documenti ipertestuali attraverso una rete di computer.",
        "Il primo mouse è stato inventato da Douglas Engelbart nel 1964 e ha introdotto un nuovo modo di interagire con i computer attraverso un'interfaccia grafica.",
        "La criptovaluta Bitcoin è stata introdotta nel 2009 da un anonimo sotto lo pseudonimo di Satoshi Nakamoto, rivoluzionando il concetto di valuta digitale decentralizzata.",
        "Il primo e-mail è stato inviato nel 1971 da Ray Tomlinson, che ha introdotto l'uso della @ per separare il nome dell'utente dal nome del computer nell'indirizzo e-mail.",
        "Il primo satellite in orbita geostazionaria, Syncom 3, è stato lanciato nel 1964 da NASA, permettendo per la prima volta le trasmissioni televisive in diretta attraverso l'Atlantico.",
        "Il supercomputer Tianhe-2, sviluppato dalla Cina, ha detenuto il record per la maggior parte delle prestazioni di calcolo dal 2013 al 2020, con una capacità di oltre 33 petaflops.",
        "La realtà aumentata è una tecnologia che sovrappone elementi digitali al mondo reale, integrando informazioni e interazioni in tempo reale attraverso dispositivi come smartphone e visori.",
        "Il software di intelligenza artificiale Deep Blue, sviluppato da IBM, ha sconfitto il campione mondiale di scacchi Garry Kasparov nel 1997, segnando un punto di svolta nella storia dell'IA."
    )
    val curiositaNaturaAmbiente = listOf(
        "La Grande Barriera Corallina, al largo delle coste del Queensland in Australia, è il più grande sistema di coralli al mondo, estendendosi per oltre 2.300 km.",
        "Il fenomeno dell'aurora boreale è causato dall'interazione tra particelle cariche provenienti dal Sole e la magnetosfera terrestre, creando spettacolari luci colorate nel cielo artico.",
        "Il Monte Everest, la montagna più alta del mondo, si trova nella catena dell'Himalaya e raggiunge un'altezza di 8.848 metri sopra il livello del mare.",
        "La foresta amazzonica, la più grande foresta pluviale del mondo, copre un'area di circa 5,5 milioni di chilometri quadrati e contiene una vasta biodiversità di specie vegetali e animali.",
        "Il deserto dell'Atacama in Cile è considerato il deserto più arido del mondo, con alcune regioni che non hanno registrato precipitazioni significative per centinaia di anni.",
        "Le cascate del Niagara, situate tra Stati Uniti e Canada, sono famose per il loro volume d'acqua impressionante e sono una delle attrazioni naturali più visitate al mondo.",
        "Il Mar Morto, situato tra Israele, Giordania e Palestina, è il punto più basso sulla superficie terrestre e contiene una concentrazione di sali così alta che gli organismi non possono sopravvivere nelle sue acque.",
        "Il fenomeno della migrazione delle farfalle Monarca vede milioni di farfalle viaggiare migliaia di chilometri dall'America del Nord al Messico per trascorrere l'inverno in poche località montuose.",
        "Il Parco Nazionale Kruger in Sudafrica è uno dei parchi più grandi e famosi per il safari in Africa, ospitando una vasta gamma di fauna selvatica tra cui elefanti, leoni, rinoceronti e leopardi.",
        "Il Lago Baikal in Siberia è il lago più profondo e antico al mondo, contenendo circa il 20% delle riserve d'acqua dolce non congelate del pianeta.",
        "Il Ghiacciaio Perito Moreno in Argentina è uno dei pochi ghiacciai al mondo che continua ad avanzare, creando spettacolari fenomeni di fratturazione e distacco dei suoi fronti di ghiaccio.",
        "Il fenomeno delle sorgenti termali nel Parco Nazionale di Yellowstone negli Stati Uniti include il famoso Old Faithful, un geyser che erutta regolarmente colonne d'acqua calda e vapore nell'aria.",
        "Il Parco Nazionale dei Fiordi Norvegesi, situato sulla costa occidentale della Norvegia, è famoso per i suoi fiordi scolpiti dai ghiacciai durante le ere glaciali, creando paesaggi spettacolari.",
        "Il fenomeno della luce di mezzanotte si verifica nelle regioni polari durante l'estate, quando il Sole non tramonta completamente sotto l'orizzonte, creando un cielo luminoso anche di notte.",
        "Il Parco Nazionale di Banff in Canada è il parco nazionale più antico del paese e comprende le Montagne Rocciose Canadesi, una delle catene montuose più spettacolari del mondo.",
        "Le Piramidi di Giza in Egitto sono una delle sette meraviglie del mondo antico e ospitano la Grande Piramide di Cheope, la più grande delle piramidi egizie.",
        "Il fenomeno delle maree è causato principalmente dall'attrazione gravitazionale della Luna e, in misura minore, del Sole sulla Terra, determinando le variazioni del livello del mare.",
        "Il fiore più grande del mondo, la Rafflesia arnoldii, può raggiungere un diametro di oltre un metro ed è noto per il suo odore sgradevole simile a carne marcia.",
        "Il Parco Nazionale Torres del Paine in Cile è famoso per i suoi paesaggi di montagne, ghiacciai, laghi e foreste, rappresentando uno degli ecosistemi più intatti del Sud America.",
        "Il fenomeno delle Città Fantasma nel deserto del Sahara sono antiche città e insediamenti abbandonati, a volte risalenti a migliaia di anni fa, testimonianza di un passato più umido nella regione."
    )
    val curiositaStoriaCultura = listOf(
        "La Biblioteca di Alessandria, fondata nel III secolo a.C., era una delle più grandi e famose biblioteche dell'antichità, contenendo migliaia di rotoli e manoscritti provenienti da tutto il mondo antico.",
        "L'Impero Romano è stato uno dei più vasti imperi della storia, estendendosi su gran parte dell'Europa, del Medio Oriente e del Nord Africa, con una durata di oltre 500 anni.",
        "La Grande Muraglia Cinese, costruita nel III secolo a.C. e ampliata nel corso dei secoli, è una delle più grandi strutture mai realizzate dall'uomo, estendendosi per oltre 21.000 km.",
        "La Rivoluzione Industriale, iniziata in Gran Bretagna nel XVIII secolo, ha segnato il passaggio da metodi di produzione manuali a processi meccanizzati, cambiando radicalmente le economie e la società mondiale.",
        "La Guerra Civile Americana, combattuta tra il 1861 e il 1865, ha visto il conflitto tra gli Stati Uniti del Nord e quelli del Sud sulla questione dell'abolizione della schiavitù e sull'unità nazionale.",
        "La costruzione della Statua della Libertà, un regalo dalla Francia agli Stati Uniti, è stata completata nel 1886 ed è diventata un simbolo universale della libertà e dell'indipendenza.",
        "Il Rinascimento italiano, a partire dal XIV secolo, è stato un periodo di grande rinnovamento culturale e artistico, caratterizzato da figure come Leonardo da Vinci, Michelangelo e Raffaello.",
        "La Rivoluzione Francese, iniziata nel 1789 con la presa della Bastiglia, ha portato alla fine del regime monarchico in Francia e alla diffusione dei principi di libertà, uguaglianza e fraternità.",
        "Il Muro di Berlino, costruito nel 1961 per dividere Berlino Est e Ovest durante la Guerra Fredda, è diventato un simbolo della divisione ideologica tra Est e Ovest.",
        "Il codice di Hammurabi, un antico codice legale babilonese scritto intorno al 1754 a.C., è uno dei primi insiemi di leggi conosciute nella storia dell'umanità, enfatizzando la giustizia e la responsabilità civile.",
        "La costruzione delle Piramidi di Teotihuacan in Messico, tra il I e il VII secolo d.C., rappresenta una delle più grandi città precolombiane e un centro di cultura e commercio nella Mesoamerica.",
        "L'Impero Mongolo, fondato da Gengis Khan nel XIII secolo, è stato uno dei più vasti imperi della storia, estendendosi dalla Cina orientale all'Europa orientale.",
        "L'Antica Grecia è stata una delle prime civiltà avanzate in Europa, influenzando la filosofia, la politica, l'arte e l'architettura per secoli, con figure come Socrate, Platone e Aristotele.",
        "La costruzione delle Grandi Piramidi di Giza in Egitto, intorno al 2500 a.C., è stata un'impresa monumentale che testimonia il potere e la tecnologia avanzata dell'antico Egitto.",
        "La Dinastia Qing, l'ultima dinastia imperiale della Cina, è durata dal 1644 al 1912, caratterizzata da periodi di prosperità e di declino nel corso dei suoi circa 268 anni di governo.",
        "La conquista dell'Impero Inca da parte degli spagnoli, guidati da Francisco Pizarro, nel XVI secolo, ha portato alla fine di una delle più grandi civiltà precolombiane delle Americhe.",
        "L'età d'oro dell'Islam, tra il VIII e il XIV secolo, è stata un periodo di grande sviluppo scientifico, letterario e culturale in tutto il mondo musulmano, con importanti contributi in matematica, astronomia, medicina e filosofia.",
        "La Guerra dei Cent'Anni, combattuta tra il 1337 e il 1453, è stata una serie di conflitti tra Inghilterra e Francia per il controllo del trono francese, segnata da eventi come la battaglia di Agincourt e la figura di Giovanna d'Arco.",
        "La costruzione delle Piramidi di Chichen Itza in Messico, tra il IX e il XII secolo d.C., rappresenta un'importante città maya e un sito di grande importanza culturale e archeologica.",
        "La scoperta della tomba di Tutankhamon nel 1922 da parte di Howard Carter ha rivelato uno dei più importanti tesori della civiltà egizia antica, conosciuto per la sua straordinaria conservazione e ricchezza di artefatti.",
        "L'Impero Bizantino, fondato nel 330 d.C. da Costantino I, ha sopravvissuto alla caduta dell'Impero Romano d'Occidente, diventando un importante centro di cultura, arte e religione cristiana ortodossa nel Mediterraneo orientale."
    )
    val curiositaArteIntrattenimento = listOf(
        "Il capolavoro di Leonardo da Vinci, la 'Gioconda', è uno dei dipinti più famosi al mondo e è ospitato al Louvre di Parigi.",
        "Il Teatro Greco di Siracusa, costruito nel V secolo a.C. in Sicilia, è uno dei teatri antichi meglio conservati e ancora utilizzati per spettacoli teatrali.",
        "Il balletto 'Il lago dei cigni', composto da Pëtr Il'ič Čajkovskij nel 1875-76, è uno dei balletti più celebri al mondo, con la coreografia originale di Marius Petipa e Lev Ivanov.",
        "La Sinfonia n. 9 di Ludwig van Beethoven, conosciuta come la 'Nona', contiene il celebre 'Inno alla gioia' nel quarto movimento, utilizzato come inno ufficiale dell'Unione Europea.",
        "Il film 'Il padrino', diretto da Francis Ford Coppola nel 1972, è considerato uno dei più grandi capolavori cinematografici della storia, vincitore di tre premi Oscar, incluso quello per il miglior film.",
        "Il romanzo 'Don Chisciotte' di Miguel de Cervantes, pubblicato nel 1605, è considerato uno dei primi romanzi moderni e una pietra miliare della letteratura spagnola e mondiale.",
        "Il compositore Wolfgang Amadeus Mozart ha composto oltre 600 opere musicali, tra cui sinfonie, concerti, opere liriche e musica da camera, durante la sua breve ma intensa carriera.",
        "Il regista Akira Kurosawa è considerato uno dei più grandi registi cinematografici giapponesi, famoso per film come 'I sette samurai' (1954) e 'Rashomon' (1950), vincitore del Leone d'Oro al Festival di Venezia.",
        "Il balletto 'Giselle', creato nel 1841 da Jean Coralli e Jules Perrot, è uno dei balletti romantici più importanti del XIX secolo, con musica di Adolphe Adam.",
        "La serie televisiva 'Game of Thrones', basata sui libri di George R.R. Martin, è stata una delle serie più popolari e influenti degli ultimi decenni, famosa per la sua trama intricata e i colpi di scena.",
        "Il pittore surrealista spagnolo Salvador Dalí è noto per opere iconiche come 'La persistenza della memoria' (1931), con i suoi orologi molli, e per la sua eccentrica personalità.",
        "La banda musicale britannica The Beatles ha dominato la scena musicale negli anni '60, con hit come 'Hey Jude', 'Let It Be' e 'Yesterday', influenzando generazioni di musicisti.",
        "La serie di film 'Il Signore degli Anelli', diretta da Peter Jackson e basata sui romanzi di J.R.R. Tolkien, ha stabilito nuovi standard nell'epica fantasy cinematografica, vincendo numerosi premi Oscar.",
        "Il romanzo 'Guerra e pace' di Lev Tolstoj, pubblicato nel 1869, è uno dei romanzi più lunghi e celebri della letteratura mondiale, ambientato durante le guerre napoleoniche in Russia.",
        "La ballerina russa Anna Pavlova è stata una delle più grandi ballerine del XX secolo, famosa per il suo virtuosismo tecnico e la sua interpretazione espressiva in balletti come 'Il morso della tarantola'.",
        "Il pittore olandese Vincent van Gogh ha prodotto oltre 2.100 opere d'arte, tra cui 'Notte stellata' (1889) e 'Il campo di grano con cipressi' (1889), esplorando temi di natura, bellezza e tormento interiore.",
        "Il musical 'Les Misérables', basato sul romanzo di Victor Hugo, è uno dei musical più longevi e amati del teatro mondiale, con celebri canzoni come 'I Dreamed a Dream' e 'Do You Hear the People Sing?'",
        "Il regista italiano Federico Fellini è considerato uno dei maestri del cinema mondiale, famoso per film come 'La dolce vita' (1960) e 'Otto e mezzo' (1963), vincitore di numerosi premi Oscar.",
        "Il compositore russo Pëtr Il'ič Čajkovskij è noto per le sue opere sinfoniche, tra cui 'Il lago dei cigni', e le sue opere liriche, come 'Eugenio Onieghin' e 'La dama di picche'.",
        "Il teatro del Globe di Londra, originariamente costruito nel 1599 e ricostruito nel 1997, è associato a William Shakespeare e ospita rappresentazioni delle sue opere classiche."
    )
    val curiositaCorpoMente = listOf(
        "Il Tai Chi è una pratica cinese antica che combina movimenti fluidi e respirazione profonda per migliorare la salute fisica e mentale.",
        "Lo yoga è una disciplina indiana che unisce posture fisiche, respirazione controllata e meditazione per migliorare la flessibilità, la forza e la tranquillità mentale.",
        "La meditazione mindfulness è una tecnica di meditazione che si concentra sull'essere presente nel momento attuale, riducendo lo stress e migliorando il benessere emotivo.",
        "L'escursionismo è un'attività all'aperto che coinvolge il camminare su percorsi naturali o montuosi, offrendo benefici per la salute cardiovascolare e il benessere mentale.",
        "Il sollevamento pesi, noto anche come allenamento con i pesi, aiuta a sviluppare la forza muscolare, migliorare la densità ossea e aumentare il metabolismo.",
        "Il Pilates è un sistema di esercizi sviluppato da Joseph Pilates che enfatizza il controllo del corpo, la respirazione e la postura per migliorare la forza, la flessibilità e l'equilibrio.",
        "Il jogging regolare può migliorare la resistenza cardiorespiratoria, bruciare calorie, ridurre lo stress e migliorare l'umore grazie alla produzione di endorfine.",
        "Il nuoto è un'attività aerobica che coinvolge tutti i principali gruppi muscolari, migliorando la resistenza, la forza muscolare e la capacità polmonare.",
        "Il sonno è essenziale per il recupero fisico e mentale, aiutando a consolidare la memoria, riparare i muscoli e migliorare l'umore e la concentrazione.",
        "La dieta mediterranea è un modello alimentare ricco di frutta, verdura, cereali integrali, pesce e olio d'oliva, associato a numerosi benefici per la salute cardiovascolare e cognitiva.",
        "Il ballo, come il ballare salsa o il tango, è un'attività divertente che migliora la coordinazione, la flessibilità e l'umore grazie alla liberazione di endorfine.",
        "Il CrossFit è un programma di fitness che combina esercizi di resistenza, sollevamento pesi e esercizi ad alta intensità per migliorare la forza, la potenza e la resistenza.",
        "Lo stretching regolare migliora la flessibilità muscolare, riduce il rischio di infortuni, allevia la tensione muscolare e migliora la postura.",
        "Il Qigong è una pratica cinese simile al Tai Chi che combina movimenti lenti, respirazione profonda e concentrazione mentale per migliorare l'equilibrio energetico e la salute generale.",
        "Il veganesimo è uno stile di vita che esclude tutti i prodotti di origine animale, promuovendo una dieta basata su frutta, verdura, cereali integrali e proteine vegetali.",
        "Il wellness è un approccio olistico alla salute che integra il benessere fisico, mentale ed emotivo attraverso pratiche come yoga, meditazione, alimentazione sana e movimento.",
        "La corsa a piedi nudi, o barefoot running, è una pratica che enfatizza il contatto diretto del piede con il terreno, ritenuta da alcuni benefica per la biomeccanica e l'equilibrio muscolare.",
        "Il massaggio terapeutico può alleviare il dolore muscolare, ridurre lo stress e migliorare la circolazione sanguigna, promuovendo il recupero fisico e il benessere generale.",
        "Il Power Yoga è una forma dinamica di yoga che combina movimenti rapidi e respirazione intensa per migliorare la forza muscolare, la flessibilità e l'equilibrio.",
        "Il termine well-being si riferisce al benessere olistico di una persona, includendo aspetti fisici, mentali, emotivi e sociali, e può essere migliorato attraverso diverse pratiche di fitness e salute."
    )
    val curiositaViaggiEsplorazioni = listOf(
        "Il viaggio di circumnavigazione di Ferdinando Magellano, completato nel 1522 da Juan Sebastián Elcano, è stato il primo viaggio documentato attorno al mondo.",
        "La Via della Seta era una rete di antiche rotte commerciali che collegavano l'Asia orientale con l'Europa, facilitando il commercio di merci come seta, spezie e porcellane.",
        "Le piramidi di Giza in Egitto sono state una delle prime sette meraviglie del mondo antico e rappresentano una delle più grandi realizzazioni dell'architettura antica.",
        "L'esploratore Marco Polo è famoso per i suoi viaggi in Asia, descritti nel libro 'Il Milione', che ha suscitato grande interesse per le terre lontane e sconosciute.",
        "La scoperta dell'America nel 1492 da parte di Cristoforo Colombo ha aperto la via all'era delle esplorazioni europee delle Americhe, cambiando il corso della storia globale.",
        "L'antica città di Petra in Giordania è famosa per la sua architettura scolpita nella roccia e la sua importanza come snodo commerciale e culturale nell'antichità.",
        "La Grande Muraglia Cinese, costruita nel III secolo a.C. e estesa nel corso dei secoli, è una delle più grandi strutture mai realizzate dall'uomo, visibile anche dallo spazio.",
        "Il Transiberiano è la ferrovia più lunga del mondo, estendendosi per oltre 9.200 km da Mosca a Vladivostok attraverso la Russia.",
        "Il viaggio di Roald Amundsen nel 1911-1912 ha visto la prima spedizione ad avere raggiunto il Polo Sud, una delle più grandi conquiste dell'età dell'esplorazione antartica.",
        "La Ruta 40 in Argentina è una delle strade più lunghe e panoramiche al mondo, estendendosi per oltre 5.000 km lungo la parte occidentale del paese, attraversando deserti e montagne.",
        "Il Cammino di Santiago di Compostela è un'antica via di pellegrinaggio in Spagna, conosciuta anche come Il Cammino di Santiago, che attrae pellegrini da tutto il mondo.",
        "La scoperta delle rovine di Machu Picchu in Perù nel 1911 da parte di Hiram Bingham ha portato alla luce una delle più significative antiche città inca, posta tra le Ande.",
        "La Grande Via della Seta Marittima, tra il II secolo a.C. e il XV secolo d.C., era una rotta commerciale che collegava l'Asia orientale con l'Africa orientale e il Medio Oriente attraverso il Mar Rosso.",
        "Il lago Baikal in Siberia è il lago più profondo e antico al mondo, contenendo circa il 20% delle riserve d'acqua dolce non congelate del pianeta.",
        "Il viaggio di Lewis e Clark negli Stati Uniti tra il 1804 e il 1806 è stato uno dei primi esplorazioni transcontinentali, documentando la geografia e la fauna dell'ovest americano.",
        "La Via Appia Antica, costruita dai Romani nel 312 a.C., è stata una delle più importanti strade romane, collegando Roma a Brindisi nel sud Italia.",
        "Il viaggio di James Cook nel XVIII secolo ha visto le prime esplorazioni europee della costa orientale dell'Australia, Nuova Zelanda e delle Hawaii, tra molte altre scoperte.",
        "La scoperta delle Isole Galapagos da parte di Charles Darwin nel 1835 ha influenzato la sua teoria dell'evoluzione per selezione naturale, basata sull'osservazione della diversità delle specie.",
        "Il viaggio di Ibn Battuta, un esploratore musulmano del XIV secolo, è stato uno dei più grandi viaggi documentati dell'età medievale, visitando terre dalla Spagna alla Cina.",
        "La Via della Seta Terrestre, tra il II secolo a.C. e il XIV secolo d.C., era una rete di rotte commerciali che collegava l'Asia orientale con l'Europa attraverso l'Asia centrale.",
        "La scoperta della Tomba di Tutankhamon nel 1922 da parte di Howard Carter ha rivelato uno dei più importanti tesori della civiltà egizia antica, conosciuto per la sua straordinaria conservazione e ricchezza di artefatti."
    )
    val curiositaCiboCucina = listOf(
        "Il sushi giapponese è tradizionalmente preparato con riso condito con aceto di riso e accompagnato da pesce crudo o altri ingredienti come verdure e uova.",
        "Il gelato, originario dell'antica Cina, è diventato popolare in Italia nel XVI secolo e si è diffuso in tutto il mondo come una delle prelibatezze più amate.",
        "Il cioccolato, derivato dai semi di cacao, è stato consumato dalle antiche civiltà mesoamericane e ha guadagnato popolarità in Europa dopo il suo arrivo dall'America Latina.",
        "Il barbecue americano, noto anche come BBQ, è una tecnica di cottura lenta e a bassa temperatura che utilizza fumo di legno per aromatizzare carni come maiale, manzo e pollo.",
        "Il couscous è un piatto a base di semola di grano duro, tipico del Nord Africa, spesso servito con verdure stufate e carne, come agnello o pollo.",
        "Il ramen è una zuppa giapponese a base di brodo, noodles di frumento, carne (spesso maiale) e altri ingredienti come uova, bambù e alghe.",
        "Il pane naan, originario dell'Asia meridionale, è un tipo di pane piatto cotto in forno tandoor, spesso servito con curry o altre salse.",
        "Il caffè espresso, originario dell'Italia, è preparato forzando acqua calda sotto pressione attraverso caffè macinato, producendo una bevanda concentrata e aromatica.",
        "Il curry indiano è un mix di spezie macinate, spesso contenente curcuma, coriandolo, cumino e pepe nero, utilizzato per preparare una varietà di piatti aromatici.",
        "Il tacos messicano è un piatto a base di tortillas di mais o farina ripiene di carne, verdure, formaggio e salse, come la salsa guacamole e la salsa salsa.",
        "Il foie gras, una prelibatezza francese, è fegato grasso d'oca o anatra, spesso servito come antipasto o ingrediente principale in cucina raffinata.",
        "Il sushi è diventato popolare in Giappone durante il periodo Edo, dal XVII al XIX secolo, sviluppando stili come il nigiri, il maki e il sashimi.",
        "La pizza napoletana, protetta dalla legislazione europea come specialità tradizionale garantita, è preparata con ingredienti freschi come pomodori San Marzano e mozzarella di bufala campana.",
        "Il gelato italiano è diventato popolare durante il Rinascimento, utilizzando neve o ghiaccio per raffreddare i dolci, come la sorbetto e il gelato.",
        "Il tè inglese è bevuta in tutto il mondo è molto apprezzata per le sue qualità è importante"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myUserId = intent.getStringExtra("userId").toString()

        initializeTopicList(userId) { success ->
            if (success) {  // La lista è stata popolata correttamente
                if (topicsList.isEmpty()) {
                    println("La lista è vuota.")
                } else {
                    println("La lista contiene ${topicsList.size} elementi: $topicsList")
                }
                addTopicsIfNeeded()
            } else {
                println("Errore durante l'inizializzazione della lista.")
            }
        }

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        userId = intent.getStringExtra("userId").toString()  //ricezione dell'id utente dall'activity precedente

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle ( this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if(savedInstanceState == null) {
            // Creazione dell'istanza del Fragment e impostazione degli argomenti
            val homeFragment = HomeFragment().apply {
                arguments = Bundle().apply {
                    putString("userId", userId)
                }
            }
            // Aggiunta del Fragment iniziale
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment).commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }

        // Register the receiver to handle button click
        val filter = IntentFilter().apply {
            addAction("ACTION_1")
            addAction("ACTION_2")
        }
        registerReceiver(notificationButtonReceiver, filter)

        // Chiamata al metodo per creare la notifica
        createNotification()
    }

    private fun addTopicsIfNeeded() {
        // Aggiungi elementi a topicsList solo se selezionati nel DB
        // Questo metodo verrà chiamato solo dopo che initializeTopicList ha completato
        if (scienza) topicsList.add("Scienza e Tecnologia")
        if (natura) topicsList.add("Natura e Ambiente")
        if (storia) topicsList.add("Storia e Cultura")
        if (arte) topicsList.add("Arte e Intrattenimento")
        if (corpo) topicsList.add("Corpo e Mente")
        if (viaggi) topicsList.add("Viaggi e Esplorazione")
        if (cibo) topicsList.add("Cibo e Cucina")

        if (topicsList.isEmpty()) {
            Toast.makeText(this, "Topic List vuota", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Topic List Creata", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notificationButtonReceiver)
    }

    private fun createNotification() {
        // Contenuto della notifica
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Verifica se il canale di notifica esiste già
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "My notification channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Costruzione dell'intento per l'azione dei bottoni
        val intentAction1 = Intent("ACTION_1")
        val intentAction2 = Intent("ACTION_2")

        val pendingIntentAction1 = PendingIntent.getBroadcast(this, 0, intentAction1,  PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val pendingIntentAction2 = PendingIntent.getBroadcast(this, 1, intentAction2,  PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val curiosityTextNotify = chooseCuriosity()
        if (curiosityTextNotify.isNotEmpty()){
            costructorNotify(notificationManager,pendingIntentAction1,pendingIntentAction2,curiosityTextNotify)
        }else{
            Toast.makeText(this, "Aggiungi preferenze di argomento nel Preference", Toast.LENGTH_SHORT).show()
        }

    }

    private fun chooseCuriosity(): String {
        // Selezione casuale di una curiosità
        var randomCuriosity : String = ""

        if (topicsList.isEmpty()) {
            return randomCuriosity  // Ritorna una stringa vuota se la lista è vuota
        }
        val randomIndex = Random.nextInt(topicsList.size)

        when (topicsList[randomIndex]) {
            "Scienza e Tecnologia" -> { //scienza
                val randomTopic = Random.nextInt(curiositaScienzaTecnologia.size)
                randomCuriosity = curiositaScienzaTecnologia[randomTopic]
            }
            "Natura e Ambiente" -> { //natura
                val randomTopic = Random.nextInt(curiositaNaturaAmbiente.size)
                randomCuriosity = curiositaNaturaAmbiente[randomTopic]
            }
            "Storia e Cultura" -> { //storia
                val randomTopic = Random.nextInt(curiositaStoriaCultura.size)
                randomCuriosity = curiositaStoriaCultura[randomTopic]
            }
            "Arte e Intrattenimento" -> { //arte
                val randomTopic = Random.nextInt(curiositaArteIntrattenimento.size)
                randomCuriosity = curiositaArteIntrattenimento[randomTopic]
            }
            "Corpo e Mente" -> { //corpo
                val randomTopic = Random.nextInt(curiositaCorpoMente.size)
                randomCuriosity = curiositaCorpoMente[randomTopic]
            }
            "Viaggi e Esplorazione" -> { //viaggi
                val randomTopic = Random.nextInt(curiositaViaggiEsplorazioni.size)
                randomCuriosity = curiositaViaggiEsplorazioni[randomTopic]
            }
            "Cibo e Cucina" -> { //cibo
                val randomTopic = Random.nextInt(curiositaCiboCucina.size)
                randomCuriosity = curiositaCiboCucina[randomTopic]
            }
        }
        return randomCuriosity
    }

    private fun costructorNotify(
        notificationManager: NotificationManager,
        pendingIntentAction1: PendingIntent,
        pendingIntentAction2: PendingIntent,
        curiosityTextNotify: String
    ) {
        // Creazione del testo espandibile per la notifica
        val bigTextStyle = NotificationCompat.BigTextStyle().bigText(curiosityTextNotify)  // Testo espandibile con il testo completo

        // Costruzione della notifica
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Curiosità! Lo sapevi che...")
            .setContentText(curiosityTextNotify)
            .setStyle(bigTextStyle)  // Applicazione del testo espandibile
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.baseline_check_24, "Lo sapevo", pendingIntentAction1)
            .addAction(R.drawable.baseline_close_24, "Non lo sapevo", pendingIntentAction2)
            .build()

        // Invio della notifica
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private val notificationButtonReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Generate a new notification when the button is pressed
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Gestione delle azioni dei bottoni qui
            when (intent?.action) {
                "ACTION_1" -> { // Azione per il primo bottone
                    Toast.makeText(context, "Button 1 clicked", Toast.LENGTH_SHORT).show()
                    notificationManager.cancel(NOTIFICATION_ID) // Elimina la notifica
                    button1Clicked = true
                }
                "ACTION_2" -> { // Azione per il secondo bottone
                    Toast.makeText(context, "Button 2 clicked", Toast.LENGTH_SHORT).show()
                    notificationManager.cancel(NOTIFICATION_ID) // Elimina la notifica
                    button2Clicked = true
                }
            }
            if (button1Clicked || button2Clicked) {
                Handler(Looper.getMainLooper()).postDelayed({
                    button1Clicked=false
                    button2Clicked=false
                    createNotification()
                }, NOTIFICATION_DELAY)

            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_home -> {
                val homeFragment = HomeFragment().apply {
                    arguments = Bundle().apply {
                        putString("userId", userId)
                    }
                }
                supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment).commit()
            }

            R.id.nav_settings -> {
                val settingsFragment = SettingsFragment().apply {
                    arguments = Bundle().apply {
                        putString("userId", userId)
                    }
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, settingsFragment)
                    .commit()
            }

            R.id.nav_preference -> {
                val preferenceFragment = PreferenceFragment().apply {
                    arguments = Bundle().apply {
                        putString("userId", userId)
                    }
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, preferenceFragment)
                    .commit()
            }

            R.id.nav_logout -> Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initializeTopicList(myUserId: String, callback: (Boolean) -> Unit) {
        val firebaseDB : FirebaseDatabase = FirebaseDatabase.getInstance("https://appcuriosity-5688a-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = firebaseDB.getReference("Users/$myUserId")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var outBack = false

                for (childSnapshot in dataSnapshot.children) {
                    val key = childSnapshot.key.toString()
                    val value = childSnapshot.value

                    if (value is Boolean) { // da sistemare
                        when (key) {
                            "scienza" -> scienza = value
                            "natura" -> natura = value
                            "storia" -> storia = value
                            "arte" -> arte = value
                            "corpo" -> corpo = value
                            "viaggi" -> viaggi = value
                            "cibo" -> cibo = value
                        }
                    }

                }
                outBack = topicsList.isNotEmpty()
                callback(outBack)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Errore nel recupero dei dati", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            onBackPressedDispatcher.onBackPressed()
        }
    }
}