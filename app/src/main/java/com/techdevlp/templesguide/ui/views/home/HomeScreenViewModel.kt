package com.techdevlp.templesguide.ui.views.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeScreenViewModel: ViewModel() {
    private val _templeList = MutableLiveData<List<TemplesData>>()
    val templeList: LiveData<List<TemplesData>> = _templeList

    private val db = Firebase.firestore
    private val collectionRef = db.collection("tirupati")

    fun getTemples() {
        collectionRef.get()
            .addOnSuccessListener { documents ->
                val documentIdList = documents.map { it.id }.toList()
                val templeList = mutableListOf<TemplesData>()
                documentIdList.forEach { id ->
                    fetchDocument(id, templeList)
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting documents: $e")
            }
    }

    private fun fetchDocument(id: String, templeList: MutableList<TemplesData>) {
        collectionRef.document(id).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val data = document.data ?: return@addOnSuccessListener
                    val templeData = parseDocumentData(data)
                    templeList.add(templeData)
                    _templeList.postValue(templeList.toList()) // Update LiveData
                } else {
                    Log.e("Firestore", "Document $id does not exist")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting document: $e")
            }
    }

    private fun parseDocumentData(data: Map<String, Any>): TemplesData {
        val name = data["name"]?.toString() ?: ""
        val city = data["city"]?.toString() ?: ""
        val imageUrl = data["image_url"]?.toString() ?: ""
        val address = data["address"]?.toString() ?: ""
        val state = data["state"]?.toString() ?: ""
        val open = data["open"]?.toString() ?: ""
        val close = data["close"]?.toString() ?: ""
        val latitude = data["lattitude"]?.toString() ?: ""
        val longitude = data["longitude"]?.toString() ?: ""
        val helpLine = data["help_line"]?.toString() ?: ""
        val story = data["story"]?.toString() ?: ""
        return TemplesData(
            name, city, imageUrl, address, state, open, close, latitude, longitude, story, helpLine
        )
    }
}