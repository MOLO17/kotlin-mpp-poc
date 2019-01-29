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

class ViewController: UIViewController {

    @IBOutlet weak var label: UITextView!
    
    private var adapter: BluetoothAdapter?

    override func viewDidLoad() {
        super.viewDidLoad()
        label.text = MainKt.getMessage()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        adapter = BluetoothAdapter { [weak self] adapter -> KotlinUnit in
            if let callback = self?.didReceiveDevices(devices:) {
                adapter.discoverDevices(callback: callback)
            }
            return KotlinUnit()
        }
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        adapter?.stopScan()
        adapter = nil
    }

    private func didReceiveDevices(devices: [BluetoothDevice]) -> KotlinUnit {
        label.text = devices.map { "ID: \($0.id)\nName: \($0.name)" }.joined(separator: "\n\n")
        return KotlinUnit()
    }
}

