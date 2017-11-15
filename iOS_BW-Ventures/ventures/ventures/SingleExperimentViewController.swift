//
//  SingleExperimentViewController.swift
//  ventures
//
//  Created by Fhict on 13/11/2017.
//  Copyright © 2017 Restart. All rights reserved.
//

import UIKit
import Firebase

class SingleExperimentViewController: UIViewController {
    
    @IBOutlet weak var LogoText: UIView!
    @IBOutlet weak var LogoTextLabel: UILabel!
    @IBOutlet weak var ExperimentTitle: UILabel!
    @IBOutlet weak var CustomersView: UIView!
    @IBOutlet weak var GoalsView: UIView!
    
    // Experiment Labels
    @IBOutlet weak var ExperimentDescription: UILabel!
    @IBOutlet weak var ExperimentDate: UILabel!
    @IBOutlet weak var ExperimentSegment: UILabel!
    @IBOutlet weak var ExperimentLocation: UILabel!
    @IBOutlet weak var ExperimentProblemHypothesis: UILabel!
    @IBOutlet weak var ExperimentLearningGoals: UILabel!
    @IBOutlet weak var ExperimentFailConditions: UILabel!
    @IBOutlet weak var ExperimentStopCondition: UILabel!
    
    let ECECEC = UIColor(red: 236/255, green: 236/255, blue: 236/255, alpha: 1.0)
    let formatter = DateFormatter()
    
    var experimentId: String?
    var experimentDict: [String: Any] = [:]
    var db:Firestore!
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        
        // Declare Firestore
        db = Firestore.firestore()
        
        // Get experiment data
        getExperiment()
        
        // Change looks
        LogoText.layer.cornerRadius = 32
        CustomersView.layer.borderWidth = 1
        GoalsView.layer.borderWidth = 1
        CustomersView.layer.borderColor = ECECEC.cgColor
        GoalsView.layer.borderColor = ECECEC.cgColor
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func getExperiment() {
        
        let userDef = UserDefaults.standard
        let ventureid = userDef.string(forKey: "ventureID")
        let docRef = db.collection("Startups").document(ventureid!).collection("Experiments").document(experimentId!)
        docRef.getDocument { (document, error) in
            if error != nil {
                
                print("Error getting experiment: \(error)")
            } else {
                
                // Get experiment data
                self.experimentDict = document!.data()
                
                // Truncate Title
                let stringInput = self.experimentDict["ExperimentName"] as! String?
                let stringInputArr = stringInput?.components(separatedBy: " ")
                var stringNeed = ""
                for string in stringInputArr! {
                    stringNeed += String(string.characters.first!)
                }
                let index = stringNeed.index(stringNeed.startIndex, offsetBy: 2)
                self.LogoTextLabel.text = stringNeed.substring(to: index)
                
                // Push data
                self.ExperimentTitle.text = self.experimentDict["ExperimentName"] as! String?
                self.ExperimentDescription.text = self.experimentDict["ExperimentSubtitle"] as! String?
                let date = self.experimentDict["DateCreated"] as! Date?
                self.formatter.dateFormat = "MMM d, yyyy"
                self.ExperimentDate.text = self.formatter.string(from: date!)
                self.ExperimentSegment.text = self.experimentDict["CustomerSegment"] as! String?
                self.ExperimentLocation.text = self.experimentDict["CustomerLocation"] as! String?
                self.ExperimentProblemHypothesis.text = self.experimentDict["ProblemHypothesis"] as! String?
                self.ExperimentLearningGoals.text = self.experimentDict["LearningGoal"] as! String?
                self.ExperimentFailConditions.text = self.experimentDict["FailCondition"] as! String?
                self.ExperimentStopCondition.text = self.experimentDict["StopCondition"] as! String?
            }
        }
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
