//
//  UsersListViewController.swift
//  Kotlin-MPP
//
//  Created by Damiano Giusti on 01/02/19.
//  Copyright © 2019 Damiano Giusti. All rights reserved.
//

import main
import Nuke
import UIKit
import TinyConstraints

private let kReuseIdentifier = "UserTableViewCell"

class UsersListViewController: UIViewController, UsersListView {

    let presenter: UsersListPresenter = UsersListPresenterKt.getUserListPresenter()

    private let datasource = UsersListDatasource()
    private lazy var tableView = UITableView()
    private lazy var progressView = UIActivityIndicatorView()

    override func loadView() {
        view = UIView()
        view.addSubview(tableView)
        view.addSubview(progressView)
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        tableView.edgesToSuperview()
        tableView.separatorStyle = .none

        tableView.register(UserTableViewCell.self, forCellReuseIdentifier: kReuseIdentifier)
        tableView.dataSource = datasource

        progressView.color = .black
        progressView.width(48)
        progressView.aspectRatio(1)
        progressView.centerInSuperview()
        progressView.startAnimating()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        presenter.attachView(v: self)
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        presenter.detachView()
    }

    // MARK: UsersListView

    func showUsers(displayableUsers: [UiUser]) {
        datasource.dataset = displayableUsers
        tableView.isHidden = false
        tableView.reloadData()
    }

    func hideUsers() {
        tableView.isHidden = true
    }

    func showLoading() {
        progressView.isHidden = false
    }

    func hideLoading() {
        progressView.isHidden = true
    }
}

// MARK: - UsersListDatasource

private class UsersListDatasource: NSObject, UITableViewDataSource {

    var dataset: [UiUser] = []

    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dataset.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let item = dataset[indexPath.row]
        if let cell = tableView.dequeueReusableCell(withIdentifier: kReuseIdentifier, for: indexPath) as? UserTableViewCell {
            cell.usernameLabel.text = item.displayName
            cell.emailLabel.text = item.email
            if let url = URL(string: item.pictureUrl) {
                Nuke.loadImage(with: url, into: cell.picImageView)
            }
            return cell
        } else {
            return UITableViewCell()
        }
    }
}
