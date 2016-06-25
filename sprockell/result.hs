module Main where

import BasicFunctions
import HardwareTypes
import Sprockell
import System
import Simulation

program :: [Instruction]
program = [    Load (ImmValue (3)) (reg1),
    Store (reg1) (DirAddr 1),
    Load (ImmValue (4)) (reg2),
    Store (reg2) (DirAddr 2),
    TestAndSet (DirAddr 0),
    TestAndSet (DirAddr 1),
    TestAndSet (DirAddr 2),
    ReadInstr (DirAddr 0),
    Receive (reg3),
    Branch (reg3) (Rel -2),
    ReadInstr (DirAddr 1),
    Receive (reg3),
    Branch (reg3) (Rel -2),
    ReadInstr (DirAddr 2),
    Receive (reg3),
    Branch (reg3) (Rel -2),
    EndProg
    ]
main = sysTest [program]