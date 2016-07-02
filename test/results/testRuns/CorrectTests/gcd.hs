module Main where

import BasicFunctions
import HardwareTypes
import Sprockell
import System
import Simulation

program :: [Instruction]
program = [    Load (ImmValue (9)) (reg1),
    Store (reg1) (DirAddr 1),
    Load (ImmValue (6)) (reg1),
    Store (reg1) (DirAddr 2),
    Load (DirAddr 2) (reg1),
    Load (ImmValue (0)) (reg2),
    Compute NEq (reg1) (reg2) (reg1),
    Branch (reg1) (Rel (2)),
    Jump (Abs 25),
    Load (DirAddr 1) (reg2),
    Load (DirAddr 2) (reg3),
    Compute Gt (reg2) (reg3) (reg2),
    Branch (reg2) (Abs (15)),
    Jump (Abs 19),
    Jump (Abs 23),
    Load (DirAddr 1) (reg3),
    Load (DirAddr 2) (reg4),
    Compute Sub (reg3) (reg4) (reg3),
    Store (reg3) (DirAddr 1),
    Load (DirAddr 2) (reg3),
    Load (DirAddr 1) (reg4),
    Compute Sub (reg3) (reg4) (reg3),
    Store (reg3) (DirAddr 2),
    Nop,
    Jump (Abs 4),
    Nop,
    ReadInstr (IndAddr (reg0)),
    Receive (reg0),
    EndProg
    ]

thread1 :: [Instruction]
thread1 = [    EndProg
    ]

thread2 :: [Instruction]
thread2 = [    EndProg
    ]

thread3 :: [Instruction]
thread3 = [    EndProg
    ]


main = sysTest [program,thread1,thread2,thread3]