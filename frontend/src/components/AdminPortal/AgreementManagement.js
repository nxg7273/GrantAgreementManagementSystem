import React, { useState, useEffect } from 'react';
import { Box, Heading, Table, Thead, Tbody, Tr, Th, Td, Button, Modal, ModalOverlay, ModalContent, ModalHeader, ModalFooter, ModalBody, ModalCloseButton, useDisclosure, useToast } from '@chakra-ui/react';
import axios from 'axios';

const AgreementManagement = () => {
  const [agreements, setAgreements] = useState([]);
  const [selectedAgreement, setSelectedAgreement] = useState(null);
  const { isOpen, onOpen, onClose } = useDisclosure();
  const toast = useToast();

  useEffect(() => {
    fetchAgreements();
  }, []);

  const fetchAgreements = async () => {
    try {
      const response = await axios.get('/api/admin/agreements');
      setAgreements(response.data);
    } catch (error) {
      console.error('Error fetching agreements:', error);
      toast({
        title: 'Error',
        description: 'Failed to fetch agreements',
        status: 'error',
        duration: 5000,
        isClosable: true,
      });
    }
  };

  const handleViewDetails = (agreement) => {
    setSelectedAgreement(agreement);
    onOpen();
  };

  const handleRegenerateAgreement = async (id) => {
    try {
      await axios.post(`/api/admin/agreements/${id}/regenerate`);
      fetchAgreements();
      toast({
        title: 'Success',
        description: 'Agreement regenerated successfully',
        status: 'success',
        duration: 5000,
        isClosable: true,
      });
    } catch (error) {
      console.error('Error regenerating agreement:', error);
      toast({
        title: 'Error',
        description: 'Failed to regenerate agreement',
        status: 'error',
        duration: 5000,
        isClosable: true,
      });
    }
  };

  const handleAutoAccept = async (id) => {
    try {
      await axios.post(`/api/admin/agreements/${id}/auto-accept`);
      fetchAgreements();
      toast({
        title: 'Success',
        description: 'Agreement auto-accepted successfully',
        status: 'success',
        duration: 5000,
        isClosable: true,
      });
    } catch (error) {
      console.error('Error auto-accepting agreement:', error);
      toast({
        title: 'Error',
        description: 'Failed to auto-accept agreement',
        status: 'error',
        duration: 5000,
        isClosable: true,
      });
    }
  };

  return (
    <Box p={5}>
      <Heading mb={6}>Agreement Management</Heading>
      <Table variant="simple">
        <Thead>
          <Tr>
            <Th>Agreement ID</Th>
            <Th>Participant</Th>
            <Th>Grant</Th>
            <Th>Status</Th>
            <Th>Actions</Th>
          </Tr>
        </Thead>
        <Tbody>
          {agreements.map((agreement) => (
            <Tr key={agreement.id}>
              <Td>{agreement.id}</Td>
              <Td>{agreement.participant.name}</Td>
              <Td>{agreement.grant.name}</Td>
              <Td>{agreement.status}</Td>
              <Td>
                <Button size="sm" colorScheme="blue" onClick={() => handleViewDetails(agreement)} mr={2}>
                  View Details
                </Button>
                <Button size="sm" colorScheme="green" onClick={() => handleRegenerateAgreement(agreement.id)} mr={2}>
                  Regenerate
                </Button>
                <Button size="sm" colorScheme="orange" onClick={() => handleAutoAccept(agreement.id)}>
                  Auto Accept
                </Button>
              </Td>
            </Tr>
          ))}
        </Tbody>
      </Table>

      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Agreement Details</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            {selectedAgreement && (
              <Box>
                <p><strong>Agreement ID:</strong> {selectedAgreement.id}</p>
                <p><strong>Participant:</strong> {selectedAgreement.participant.name}</p>
                <p><strong>Grant:</strong> {selectedAgreement.grant.name}</p>
                <p><strong>Status:</strong> {selectedAgreement.status}</p>
                <p><strong>Created At:</strong> {new Date(selectedAgreement.createdAt).toLocaleString()}</p>
                <p><strong>Updated At:</strong> {new Date(selectedAgreement.updatedAt).toLocaleString()}</p>
              </Box>
            )}
          </ModalBody>
          <ModalFooter>
            <Button colorScheme="blue" mr={3} onClick={onClose}>
              Close
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </Box>
  );
};

export default AgreementManagement;
