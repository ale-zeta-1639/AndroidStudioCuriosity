package com.example.appcuriosity

class User {
    private var mail: String? = null
    private var first: Boolean = false
    private var scienza: Boolean = false
    private var natura: Boolean = false
    private var storia: Boolean = false
    private var arte: Boolean = false
    private var corpo: Boolean = false
    private var viaggi: Boolean = false
    private var cibo: Boolean = false
    private var formatoOra : Boolean = true
    private var valoreOra : String = "1"
    private var countConosciuti : Float = 0f
    private var countSconosciuti : Float = 0f

    constructor(mail: String?){
        this.mail = mail
        this.first = false
    }

    fun getCountConosciuti(): Float{return countConosciuti}
    fun getCountSconosciuti(): Float{return countSconosciuti}
    fun setCountConosciuti(value:Float) {countConosciuti=value}
    fun setCountSconosciuti(value:Float) {countSconosciuti=value}

    fun getMail(): String { return mail.toString() }
    fun getFirst(): Boolean { return first}
    fun setFirst(value:Boolean) { first=value}

    fun getScienza():Boolean {return scienza}
    fun getNatura():Boolean {return natura}
    fun getStoria():Boolean {return storia}
    fun getArte():Boolean {return arte}
    fun getCorpo():Boolean {return corpo}
    fun getViaggi():Boolean {return viaggi}
    fun getCibo():Boolean {return cibo}
    fun getFormatoOra():Boolean {return formatoOra}
    fun getValoreOra():String {return valoreOra}


    fun setScienza(value:Boolean) {scienza=value}
    fun setNatura(value:Boolean) {natura=value}
    fun setStoria(value:Boolean) {storia=value}
    fun setArte(value:Boolean) {arte=value}
    fun setCorpo(value:Boolean) {corpo=value}
    fun setViaggi(value:Boolean) {viaggi=value}
    fun setCibo(value:Boolean) {cibo=value}
    fun setFormatoOra(value:Boolean) {formatoOra=value}
    fun setValoreOra(value:String) {valoreOra=value}





}