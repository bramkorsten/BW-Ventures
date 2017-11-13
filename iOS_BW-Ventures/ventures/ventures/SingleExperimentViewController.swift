//
//  SingleExperimentClass.swift
//  ventures
//
//  Created by Fhict on 13/11/2017.
//  Copyright © 2017 Restart. All rights reserved.
//

import UIKit

class SingleExperimentViewController: UIViewController {
    
    @IBOutlet weak var LogoText: UIView!
    @IBOutlet weak var LogoTextLabel: UILabel!
    
    var experimentVar: Int = 0
    
    override func viewWillAppear(_ animated: Bool) {
        
        LogoText.layer.cornerRadius = 32
    }
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        print(experimentVar)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
