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
    
    private var adapter: BluetoothAdapter!

    override func viewDidLoad() {
        super.viewDidLoad()
        label.text = MainKt.getMessage()
        adapter = BluetoothAdapter { adapter -> KotlinUnit in
            adapter.discoverDevices(callback: self.didReceiveDevices(devices:))
            return KotlinUnit()
        }

    }


    private func didReceiveDevices(devices: [BluetoothDevice]) -> KotlinUnit {
        label.text = devices.map { "ID: \($0.id)\nName: \($0.name)" }.joined(separator: "\n\n")
        return KotlinUnit()
    }

}

