//
//  ViewController.swift
//  Kotlin-MPP
//
//  Created by Damiano Giusti on 29/01/19.
//  Copyright © 2019 Damiano Giusti. All rights reserved.
//

import UIKit
import main
import CoreBluetooth

class ViewController: UIViewController, DevicesListView {

    @IBOutlet weak var label: UITextView!
    
    private let presenter = DevicesListPresenter(bluetoothAdapter: BluetoothAdapter())

    override func viewDidLoad() {
        super.viewDidLoad()
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
}

