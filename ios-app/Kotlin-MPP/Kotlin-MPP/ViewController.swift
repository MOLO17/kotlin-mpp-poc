//
//  ViewController.swift
//  Kotlin-MPP
//
//  Created by Damiano Giusti on 29/01/19.
//  Copyright © 2019 Damiano Giusti. All rights reserved.
//

import main
import TinyConstraints
import UIKit

class ViewController: UIViewController, DevicesListView {
    
    private let presenter = DevicesListPresenter(bluetoothAdapter: BluetoothAdapter())

    private lazy var label = UITextView()
    private lazy var snackbar = Snackbar()

    override func loadView() {
        view = UIView()
        view.addSubview(label)
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        label.edgesToSuperview()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        presenter.attachView(v: self)
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        presenter.detachView()
    }

    func showDevices(devices: [UiDevice]) {
        label.text = devices.map { $0.displayableContent }.joined(separator: "\n\n")
    }

    func showMessage(message: String) {
        snackbar.show(message: message)
    }
}

