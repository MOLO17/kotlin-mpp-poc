//
//  UserTableViewCell.swift
//  Kotlin-MPP
//
//  Created by Damiano Giusti on 01/02/19.
//  Copyright © 2019 Damiano Giusti. All rights reserved.
//

import UIKit

class UserTableViewCell: UITableViewCell {

    private(set) lazy var picImageView = UIImageView()
    private(set) lazy var usernameLabel = UILabel()
    private(set) lazy var emailLabel = UILabel()

    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        contentView.addSubview(picImageView)
        contentView.addSubview(usernameLabel)
        contentView.addSubview(emailLabel)
        selectionStyle = .none

        picImageView.leadingToSuperview(contentView.layoutMarginsGuide.leadingAnchor)
        picImageView.topToSuperview(contentView.layoutMarginsGuide.topAnchor)
        picImageView.width(48)
        picImageView.aspectRatio(1)

        usernameLabel.leadingToTrailing(of: picImageView, offset: 8)
        usernameLabel.topToSuperview(contentView.layoutMarginsGuide.topAnchor)
        usernameLabel.trailingToSuperview(contentView.layoutMarginsGuide.trailingAnchor)

        emailLabel.leading(to: usernameLabel)
        emailLabel.trailing(to: usernameLabel)
        emailLabel.topToBottom(of: usernameLabel, offset: 8)
        emailLabel.bottomToSuperview()
    }

    required init?(coder aDecoder: NSCoder) {
        fatalError()
    }
}
