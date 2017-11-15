//
//  PeopleViewController.swift
//  ventures
//
//  Created by Fhict on 14/11/2017.
//  Copyright © 2017 Restart. All rights reserved.
//

import UIKit
import Firebase

class PeopleCell: UITableViewCell {
    
    @IBOutlet weak var peopleName: UILabel!
    @IBOutlet weak var peopleDescription: UILabel!
    @IBOutlet weak var peopleTag: UILabel!
    @IBOutlet weak var peopleTagBackground: UIView!
    @IBOutlet weak var peopleOptions: UIImageView!
    
}

class PeopleViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

}
