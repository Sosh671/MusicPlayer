package com.example.musicplayer.mappers

interface MapperInterface<DATA, DOMAIN> {
    fun mapToDomain(entity: DATA): DOMAIN
    fun mapToEntity(domain: DOMAIN): DATA
}