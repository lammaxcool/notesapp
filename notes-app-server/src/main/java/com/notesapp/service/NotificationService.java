package com.notesapp.service;

public interface NotificationService<T> {

    void onCreate(T entity);

    void onRead(T entity);

    void onUpdate(T entity);

    void onDelete(T entity);
}