//
//  Snackbar.swift
//  ConnectionWFM3
//
//  Created by Matteo Sist on 28/11/2018.
//

import MaterialComponents.MaterialSnackbar

/// Class wrapper of MaterialSnackbar
public class Snackbar {

    // MARK: - Public functions

    func show(message: String) {
        let message = MDCSnackbarMessage(text: message)
        message.duration = 10

        manager.show(message)
    }

    // MARK: - Private properties

    private lazy var manager: MDCSnackbarManager = {
        let m = MDCSnackbarManager()
        m.messageFont = UIFont.regular(ofSize: 14)

        return m
    }()
}
